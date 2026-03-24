package cn.com.edtechhub.nftrpc;

import cn.com.edtechhub.nftbase.exception.BizException;
import cn.com.edtechhub.nftbase.exception.SystemException;
import cn.com.edtechhub.nftbase.response.BaseResponse;
import cn.com.edtechhub.nftbase.response.ResponseCode;
import cn.com.edtechhub.nftbase.utils.BeanValidator;
import com.alibaba.fastjson2.JSON;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Facade 注解的切面处理类，实现了统一统计进行参数校验及异常捕获的特定功能，确保远程调用的返回不是异常而是统一错误的形式结果
 * 注意 JSR303 提供的注解只是声明，除了 SpringMVC 会帮我们自己接入控制器的参数，大部分情况下我们需要自己实现这个拦截参数的逻辑
 *
 * @author limou3434
 */
@Component
@Order(Integer.MIN_VALUE) // 让这个类在 Spring 里执行顺序排到最优先执行
@Aspect
@Slf4j
public class FacadeAspect {

    /**
     * Facade 注解的切面处理类
     *
     * @param pjp AOP 核心对象，代表被拦截的目标方法，包含方法签名、入参、目标对象等关键信息
     * @return 兼容所有被拦截方法的返回类型（因为不同被 Facade 注解修饰的方法返回值可能不同）
     * @throws Exception 抛出的异常
     */
    @Around("@annotation(cn.hollis.nft.turbo.rpc.facade.Facade)")
    public Object facade(ProceedingJoinPoint pjp) throws Exception {
        // 开始计时这个校验过程需要消耗的时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 获取被拦截的目标方法入参
        Object[] args = pjp.getArgs();

        // 获取被拦截的目标方法签名（是方法的唯一标识卡片，只包含方法的静态标识信息（名、参数、返回值类型），是轻量级的 “摘要信息”）
        MethodSignature signature = (MethodSignature) pjp.getSignature();

        // 获取被拦截的目标方法对象（是方法的完整说明手册，基于签名扩展，包含方法的所有元数据（注解、所属类、访问修饰符、异常声明等），是可操作的 “全量信息”）
        Method method = signature.getMethod();

        // 获取被拦截的目标方法返回值类型
        Class<?> returnType = method.getReturnType();

        // 循环遍历所有参数，根据注解规则来对每一个参数进行校验
        for (Object parameter : args) {
            try {
                BeanValidator.validateObject(parameter);
            } catch (ValidationException e) {
                log.error(this.getInfoMessage("校验失败", stopWatch, method, args, null, e), e);
                return getFailedResponse(returnType, e);
            }
        }

        // 校验通过后就让目标方法开始执行
        try {
            // 开始执行
            Object response = pjp.proceed();
            log.info(this.getInfoMessage("执行结束", stopWatch, method, args, response, null));

            // 返回结果
            this.enrichObject(response);
            return response; // TODO：其实我有点无法理解这种做法...我觉得直接返回结果就行了，然后由程序员来规定响应结构
        } catch (Throwable throwable) {
            // 记录执行异常的日志后返回一个兜底的通用失败结果
            log.error(this.getInfoMessage("执行异常", stopWatch, method, args, null, throwable), throwable);
            return this.getFailedResponse(returnType, throwable);
        }
    }

    /**
     * 统一格式输出，方便做日志统计
     * <p>
     * *** 如果调整此处的格式，需要同步调整日志监控 ***
     *
     * @param action    行为
     * @param stopWatch 耗时
     * @param method    方法
     * @param args      参数
     * @param response  响应
     * @return 拼接后的字符串
     */
    private String getInfoMessage(String action, StopWatch stopWatch, Method method, Object[] args, Object response, Throwable exception) {
        // 拼接字符串
        StringBuilder stringBuilder = new StringBuilder(action);

        stringBuilder.append(", method = ");
        stringBuilder.append(method.getName());

        stringBuilder.append(", cost = ");
        stringBuilder.append(stopWatch.getTime()).append(" ms");

        if (response instanceof BaseResponse) {
            stringBuilder.append(", success = ");
            stringBuilder.append(((BaseResponse) response).getSuccess());
        }

        if (exception != null) {
            stringBuilder.append(", success = ");
            stringBuilder.append(false);
        }

        stringBuilder.append(", args = ");
        try {
            // 把参数转化为 JSON 结构后添加到格式化字符串中
            stringBuilder.append(JSON.toJSONString(Arrays.toString(args))); // 这里转化的过程中可能会失败，所以这里用 try-catch 捕获异常
        } catch (Exception e) {
            log.error("转化 JSON 结构失败", e);
        }

        if (response != null) {
            stringBuilder.append(", resp = ");
            stringBuilder.append(JSON.toJSONString(response));
        }

        if (exception != null) {
            stringBuilder.append(" ,exception = ");
            stringBuilder.append(exception.getMessage());
        }

        if (response instanceof BaseResponse baseResponse) { // 这里是模式变量的语法，如果判断为真，直接将 response 强转为 BaseResponse 类型并赋值给 baseResponse 变量
            if (!baseResponse.getSuccess()) {
                stringBuilder.append(" , execute_failed");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 将 response 的信息补全，主要是 code
     *
     * @param response 响应对象
     */
    private void enrichObject(Object response) {
        if (response instanceof BaseResponse) {
            // 响应布尔为成功
            if (((BaseResponse) response).getSuccess()) {
                // 如果状态是成功的，需要将未设置的 responseCode 设置成 SUCCESS
                if (StringUtils.isEmpty(((BaseResponse) response).getResponseCode())) {
                    ((BaseResponse) response).setResponseCode(ResponseCode.SUCCESS.name());
                }
            }
            // 响应布尔为失败
            else {
                // 如果状态是成功的，需要将未设置的 responseCode 设置成 BIZ_ERROR
                if (StringUtils.isEmpty(((BaseResponse) response).getResponseCode())) {
                    ((BaseResponse) response).setResponseCode(ResponseCode.BIZ_ERROR.name()); // TODO：这样的话，有的方法就无法进行使用注解来做参数验证，因为响应编码无法被修改
                }
            }
        }
    }

    /**
     * 定义并返回一个通用的失败响应
     *
     * @param returnType 返回值类型
     * @param throwable  异常
     * @return 响应对象
     * @throws NoSuchMethodException 找不到构造函数
     * @throws IllegalAccessException 访问权限不够
     * @throws InvocationTargetException 调用目标方法异常
     * @throws InstantiationException 实例化异常
     */
    private Object getFailedResponse(Class<?> returnType, Throwable throwable) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 如果返回值的类型为 BaseResponse 的子类，则创建一个通用的失败响应
        if (returnType.getDeclaredConstructor().newInstance() instanceof BaseResponse response) {

            // 设置响应布尔
            response.setSuccess(false);

            // 设置响应编码和设置响应信息
            if (throwable instanceof BizException bizException) {
                response.setResponseMessage(bizException.getErrorCode().getMessage());
                response.setResponseCode(bizException.getErrorCode().getCode());
            } else if (throwable instanceof SystemException systemException) {
                response.setResponseMessage(systemException.getErrorCode().getMessage());
                response.setResponseCode(systemException.getErrorCode().getCode());
            } else {
                response.setResponseMessage(throwable.toString());
                response.setResponseCode(ResponseCode.BIZ_ERROR.name());
            }

            // 放回响应体
            return response;
        }

        log.error("未能获取失败响应，返回类型 ({}) 不是 BaseResponse 的实例", returnType);
        return null;
    }

}
