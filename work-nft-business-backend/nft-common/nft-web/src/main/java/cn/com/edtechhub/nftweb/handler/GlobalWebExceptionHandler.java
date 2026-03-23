package cn.com.edtechhub.nftweb.handler;

import cn.com.edtechhub.nftbase.exception.BizException;
import cn.com.edtechhub.nftbase.exception.SystemException;
import cn.com.edtechhub.nftweb.vo.Result;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * 统一拦截并且处理 Controller 接口中出现的所有异常
 * 不过由于我们是多模块的 Spring 应用，这里就必须保证想要使用到这个异常处理的启动类扫描得到
 * SpringBoot 的扫描逻辑本质是以 “启动类” 所在包为 “根”，递归扫描所有子包下的注解类 @Controller、@Service、@Component、@ControllerAdvice
 * 因此如果其他模块想使用到这个异常处理类，那么这个类必须使用注解 @SpringBootApplication(scanBasePackages = {"cn.com.edtechhub.当前模块", "cn.com.edtechhub.nftweb"}) 来保证把自己扫描进去
 * 后续如果考虑自动引入的话，可以使用 resources/META-INF/spring
 * TODO：还不确认哪些模块内部有控制器，这个后续再来使用扫描注解
 *
 * @author limou3434
 */
@ControllerAdvice // Spring MVC 提供的核心注解，专门用于全局统一处理控制器 Controller 的通用逻辑，可以理解为控制器的全局增强器
@Slf4j
public class GlobalWebExceptionHandler {

    /**
     * 自定义参数异常处理器
     *
     * @param methodArgumentNotValidException 参数异常
     * @return Map<String, String>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) // 指定拦截的异常类型
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 设置 HTTP 响应状态码为 400（请求参数错误）
    @ResponseBody
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
        // 日志记录
        log.error("参数异常 methodArgumentNotValidException", methodArgumentNotValidException);

        // 创建错误信息 Map（预期容量 1，优化性能）
        Map<String, String> errors = Maps.newHashMapWithExpectedSize(1);

        // 遍历所有参数校验错误，提取 “字段名称+错误信息”
        methodArgumentNotValidException
                .getBindingResult() // 从异常中取出校验结果对象
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField(); // 这里实际上是用父类 ObjectError 去接受了实际为 FieldError 的变量，因此这里是多态的使用
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        // 返回由参数错误组成的 JSON 响应
        return errors;
    }

    /**
     * 自定义业务异常处理器
     *
     * @param bizException 业务异常
     * @return Result<?>
     */
    @ExceptionHandler(BizException.class) // 指定拦截的异常类型
    @ResponseStatus(HttpStatus.OK) // HTTP 响应状态码 200（业务异常属于预期内，不用 500）
    @ResponseBody
    public Result<?> exceptionHandler(BizException bizException) {
        // 日志记录
        log.error("业务异常 bizException", bizException);

        // 创建载荷
        Result<?> result = new Result<>();

        // 填充载荷
        result.setSuccess(false);
        result.setCode(bizException.getErrorCode().getCode());
        if (bizException.getMessage() == null) { // TODO：这里不同的消息到底有什么鬼区别，我觉得需要使用名字来做区分
            result.setMessage(bizException.getErrorCode().getMessage());
        } else {
            result.setMessage(bizException.getMessage());
        }

        // 返回载荷
        return result;
    }

    /**
     * 自定义系统异常处理器
     *
     * @param systemException 系统异常
     * @return Result<?>
     */
    @ExceptionHandler(SystemException.class) // 指定拦截的异常类型
    @ResponseStatus(HttpStatus.OK) // 同样返回 200（前端统一处理，提高用户信任度）
    @ResponseBody
    public Result<?> systemExceptionHandler(SystemException systemException) {
        // 日志记录
        log.error("系统异常 systemException.", systemException);

        // 创建载荷
        Result<?> result = new Result<>();

        // 填充载荷
        result.setSuccess(false);
        result.setCode(systemException.getErrorCode().getCode());
        if (systemException.getMessage() == null) {
            result.setMessage(systemException.getErrorCode().getMessage()); // TODO：这里不同的消息到底有什么鬼区别，我觉得需要使用名字来做区分
        } else {
            result.setMessage(systemException.getMessage());
        }

        // 返回载荷
        return result;
    }

    /**
     * 自定义全部异常处理器
     *
     * @param throwable 全部异常
     * @return Result<?>
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<?> throwableHandler(Throwable throwable) {
        // 日志记录
        log.error("throwable occurred.", throwable);

        // 创建载荷
        Result<?> result = new Result<>();

        // 填充载荷
        result.setCode("位置");
        result.setMessage("哎呀，当前网络比较拥挤，请您稍后再试 /ᐠﹷ ‸ ﹷ ᐟ\\ﾉ");
        result.setSuccess(false);

        // 返回载荷
        return result;
    }

}
