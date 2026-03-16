package cn.com.edtechhub.nftbase.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目内部自定义的业务异常
 *                       +------------------------+
 *                       |        Throwable       | 所有异常的根类
 *                       +------------------------+
 *                                   |
 *            +--------------------------------------------------------+
 *            |                                                        |
 *   +--------------------+                            +-------------------------------+
 *   |       Exception    | 尚可捕获处理错误              |            Error              | 不可捕获系统错误
 *   +--------------------+                            +-------------------------------+
 *            |                                                        |
 *   +------------------------------------------+      +-----------------------------------------+
 *   | RuntimeException...（以及其他编译时就会抛出） |      | OutOfMemoryError、StackOverflowError... |
 *   +------------------------------------------+      +-----------------------------------------+
 *
 * @author limou3434
 * TODO：为什么不创建一个异常基类呢？都有响应基类了...
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException {

    /**
     * 异常代码接口实例
     */
    private ErrorCode errorCode;

    /**
     * 对齐 RuntimeException 的一系列构造函数
     *
     * @param errorCode
     */
    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        log.error("异常编码: {}, 异常信息: {}", errorCode.getCode(), errorCode.getMessage());
    }
    public BizException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
        log.error("异常编码: {}, 异常信息: {}, 开发提示 {}", errorCode.getCode(), errorCode.getMessage(), message);
    }
    public BizException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
        log.error("异常编码: {}, 异常信息: {}, 开发提示 {}, 具体成因 {}", errorCode.getCode(), errorCode.getMessage(), message, cause.getMessage());
    }
    public BizException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
        log.error("异常编码: {}, 异常信息: {}, 具体成因 {}", errorCode.getCode(), errorCode.getMessage(), cause.getMessage());
    }
    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        log.error("异常编码: {}, 异常信息: {}, 具体成因 {}, {}", errorCode.getCode(), errorCode.getMessage(), cause.getMessage(), "已经开启 “收集次要异常” 和 “写入堆栈轨迹”");
    }

}
