package cn.com.edtechhub.nftbase.exception;

import lombok.Getter;

/**
 * 业务异常可选的异常代码
 *
 * @author limou3434
 */
@Getter
public enum BizErrorCode implements ErrorCode {

    /**
     * HTTP 客户端错误
     */
    HTTP_CLIENT_ERROR("HTTP_CLIENT_ERROR", "HTTP 客户端错误"),

    /**
     * HTTP 服务端错误
     */
    HTTP_SERVER_ERROR("HTTP_SERVER_ERROR", "HTTP 服务端错误"),

    /**
     * 不允许重复发送通知
     */
    SEND_NOTICE_DUPLICATED("SEND_NOTICE_DUPLICATED", "不允许重复发送通知"),

    /**
     * 通知保存失败
     */
    NOTICE_SAVE_FAILED("NOTICE_SAVE_FAILED", "通知保存失败"),

    /**
     * 状态机转换失败
     */
    STATE_MACHINE_TRANSITION_FAILED("STATE_MACHINE_TRANSITION_FAILED", "状态机转换失败"),

    /**
     * 重复请求
     */
    DUPLICATED("DUPLICATED", "重复请求"),

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
    BizErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
