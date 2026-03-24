package cn.com.edtechhub.nftlock;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面
 *
 * @author limou3434
 */
@Component
@Order(Integer.MIN_VALUE + 1) // TODO：研究为什么部分类需要手动设置加载顺序
@Aspect
@Slf4j
public class DistributeLockAspect {

    /**
     * redisson 客户端
     */
    private final RedissonClient redissonClient;

    /**
     * 构造函数，自动获取 Spring 配置好的 redisson 客户端
     *
     * @param redissonClient redisson 客户端
     */
    public DistributeLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * DistributeLock 注解的切面处理类
     *
     * @param pjp AOP 核心对象，代表被拦截的目标方法，包含方法签名、入参、目标对象等关键信息
     * @return 兼容所有被拦截方法的返回类型（因为不同被 DistributeLock 注解修饰的方法返回值可能不同）
     * @throws Exception 抛出的异常
     */
    @Around("@annotation(cn.com.edtechhub.nftlock.DistributeLock)")
    public Object process(ProceedingJoinPoint pjp) throws Exception {
        Object response = null;

        // 获取被拦截的目标方法签名（是方法的唯一标识卡片，只包含方法的静态标识信息（名、参数、返回值类型），是轻量级的 “摘要信息”）
        MethodSignature signature = (MethodSignature) pjp.getSignature();

        // 获取被拦截的目标方法对象（是方法的完整说明手册，基于签名扩展，包含方法的所有元数据（注解、所属类、访问修饰符、异常声明等），是可操作的 “全量信息”）
        Method method = signature.getMethod();

        // 获取到注解中所有的配置值
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);

        // 获取到注解参数方便后续进行处理
        String key = distributeLock.key();
        String keyExpression = distributeLock.keyExpression();
        String scene = distributeLock.scene();
        int expireTime = distributeLock.expireTime();
        int waitTime = distributeLock.waitTime();

        // 处理参数
        if (DistributeLockConstant.NONE_KEY.equals(key)) {
            // 处理用户错误传参的情况
            if (DistributeLockConstant.NONE_KEY.equals(keyExpression)) {
                throw new DistributeLockException("在没有给注解传入 key 参数的情况下，至少需要指定 keyExpression 参数才能使用本注解");
            }

            // 把 keyExpression 字符串转换成实际可以执行的 SpEL 表达式
            Expression expression = new SpelExpressionParser().parseExpression(keyExpression);

            // 获取运行时参数的名称
            String[] parameterNames = new StandardReflectionParameterNameDiscoverer().getParameterNames(method);

            // 解析出实际的参数名和参数值到 EvaluationContext 上下文环境中，当作一个 map 去理解
            EvaluationContext context = new StandardEvaluationContext(); // 创建一个空的 EvaluationContext 上下文环境用来存放解析出来的参数
            Object[] args = pjp.getArgs(); // 获取调用方法中所有的参数值
            if (parameterNames != null) {
                for (int i = 0; i < parameterNames.length; i++) {
                    context.setVariable(parameterNames[i], args[i]);
                }
            }

            // 根据解析表达式传入上下文来获取最终的结果作为新的 key 参数
            key = String.valueOf(expression.getValue(context)); // 其实就是把用户指定的参数具体值作为 key 来使用
        }

        // 组装分布式锁的键名
        String lockKey = scene + "#" + key;

        // 从 Redisson 中获取指定 lockKey 分布式锁
        RLock rLock= redissonClient.getLock(lockKey);
        try {
            // 创建该变量时默认设置为 false，该变量表示锁是否成功的状态
            boolean lockResult;

            // 如果分布式锁的没设置等待时间（没获取到则一直等待获取到分布锁）
            if (waitTime == DistributeLockConstant.DEFAULT_WAIT_TIME) {
                // 如果分布式锁的没有过期时间
                if (expireTime == DistributeLockConstant.DEFAULT_EXPIRE_TIME) {
                    log.info("开始获取分布式锁，分布式锁的名称 lockKey 为：{}", lockKey);
                    rLock.lock();
                }
                // 如果分布式锁的存在过期时间
                else {
                    log.info("开始获取分布式锁，分布式锁的名称 lockKey 为：{}，过期时间（ms）：{}", lockKey, expireTime);
                    rLock.lock(expireTime, TimeUnit.MILLISECONDS);
                }
                lockResult = true; // 会一直阻塞到获取到分布锁成功，但是 lock() 方法不会返回布尔，只能手动书写
            }
            // 如果分布锁的有设置等待时间（没获取到则一直等待获取到分布锁，但是时间一到就停止尝试）
            else {
                // 如果分布式锁的没有过期时间
                if (expireTime == DistributeLockConstant.DEFAULT_EXPIRE_TIME) {
                    log.info("开始获取分布式锁，分布式锁的名称 lockKey 为：{}（尝试时间 {}）", lockKey, waitTime);
                    lockResult = rLock.tryLock(waitTime, TimeUnit.MILLISECONDS);
                }
                // 如果分布式锁的存在过期时间
                else {
                    log.info("开始获取分布式锁，分布式锁的名称 lockKey 为：{}，过期时间（ms）：{}（尝试时间 {}）", lockKey, expireTime, waitTime);
                    lockResult = rLock.tryLock(waitTime, expireTime, TimeUnit.MILLISECONDS);
                    // 这里无需设置分布锁是否设置成功，因为 tryLock() 方法会返回布尔值，如果成功则返回 true，失败则返回 false
                }
            }

            // 判断锁是否成功
            if (!lockResult) {
                log.warn("获取分布式锁 {} 失败", lockKey);
                throw new DistributeLockException("获取分布式锁" + lockKey + "失败");
            }
            log.info("获取分布式锁 {} 成功，锁剩余的存活时间为 {}", lockKey, rLock.remainTimeToLive());

            // 继续执行被拦截的方法
            response = pjp.proceed();
        } catch (Throwable e) {
            throw new Exception(e);
        } finally {
            // 方法执行结束后需要释放当前线程自己加的分布锁 TODO：关于 Java 线程的问题以后研究一下
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
                log.info("释放分布式锁 {} 成功", lockKey);
            }
        }
        return response;
    }
    
}
