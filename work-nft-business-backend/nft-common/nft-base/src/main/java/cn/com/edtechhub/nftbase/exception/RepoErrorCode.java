package cn.com.edtechhub.nftbase.exception;

import lombok.Getter;

/**
 * 存储错误码
 *
 * @author limou3434
 */
@Getter
public enum RepoErrorCode implements ErrorCode {

    /**
     * 未知错误
     */
    UNKNOWN_ERROR("UNKNOWN_ERROR", "未知错误"),

    /**
     * 数据库插入失败
     */
    INSERT_FAILED("INSERT_FAILED", "数据库插入失败"),

    /**
     * 数据库更新失败
     */
    UPDATE_FAILED("UPDATE_FAILED", "数据库更新失败");

    /**
     * 错误编码
     */
    @Getter
    private final String code;

    /**
     * 错误信息
     */
    @Getter
    private final String message;

    /**
     * 枚举内部构造函数
     *
     * @param code
     * @param message
     */
    RepoErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
