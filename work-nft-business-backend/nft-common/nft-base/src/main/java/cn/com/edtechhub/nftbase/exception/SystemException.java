package cn.com.edtechhub.nftbase.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目内部自定义的系统异常
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
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemException extends RuntimeException {

    /**
     * 异常代码接口实例
     */
    private ErrorCode errorCode;

    /**
     * 对齐 RuntimeException 的一系列构造函数
     *
     * @param errorCode
     */
    public SystemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public SystemException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public SystemException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    public SystemException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }
    public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

}
