
package cn.com.edtechhub.nftbase.exception;

import lombok.Getter;

/**
 * 远调异常可选的异常代码
 *
 * @author limou3434
 */
@Getter
public enum RemoteCallErrorCode implements ErrorCode {

    /**
     * 远程调用返回结果为空
     */
    REMOTE_CALL_RESPONSE_IS_NULL("REMOTE_CALL_RESPONSE_IS_NULL", "远程调用返回结果为空"),

    /**
     * 远程调用返回结果失败
     */
    REMOTE_CALL_RESPONSE_IS_FAILED("REMOTE_CALL_RESPONSE_IS_FAILED", "远程调用返回结果失败");

    /**
     * 错误编码
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    /**
     * 枚举内部构造函数
     *
     * @param code
     * @param message
     */
    RemoteCallErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
