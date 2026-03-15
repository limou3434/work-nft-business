
package cn.com.edtechhub.nftbase.exception;

import lombok.Getter;

/**
 * 限流错误码
 *
 * @author limou3434
 */
@Getter
public enum BlockErrorCode implements ErrorCode {

    /**
     * 请求被限流
     */
    REQUEST_IS_BLOCKED("REQUEST_IS_BLOCKED", "请求被限流啦~");

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
    BlockErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
