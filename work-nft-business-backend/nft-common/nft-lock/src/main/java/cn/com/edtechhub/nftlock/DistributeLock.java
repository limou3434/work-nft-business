package cn.com.edtechhub.nftlock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁自定义注解
 *
 * @author limou3434
 */
@Target(ElementType.METHOD) // 该注解只能加在方法上 TODO：可以试图在其他注解上也限制好这些条件
@Retention(RetentionPolicy.RUNTIME) // 该注解在运行时才生效
public @interface DistributeLock {

    /**
     * 锁的场景
     *
     * @return String
     */
    String scene();

    /**
     * 锁的键名，优先取 key()，没有就取 keyExpression()
     *
     * @return String
     */
    String key() default DistributeLockConstant.NONE_KEY;

    /**
     * SPEL 表达式键名
     * 其实就是 Spring 表达式，超级简单
     * 写 "#id" 就代表获取参数 id 的值作为锁的键名
     * 写 "#insertResult.id" 就代表获取插入参数 insertResult 内部成员 id 的值作为锁的键名
     *
     * @return String
     */
    String keyExpression() default DistributeLockConstant.NONE_KEY;

    /**
     * 毫秒级别的等锁时长，默认情况下不设置等待时长，会一直等待直到获取到锁
     *
     * @return int
     */
    int waitTime() default DistributeLockConstant.DEFAULT_WAIT_TIME;

    /**
     * 毫秒级别的超时时间，默认情况下不设置超时时间，会自动续期
     *
     * @return int
     */
    int expireTime() default DistributeLockConstant.DEFAULT_EXPIRE_TIME;

}
