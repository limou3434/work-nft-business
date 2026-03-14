package cn.com.edtechhub.nftbase.response;

/**
 * 响应编码
 *
 * @author limou3434
 */
public enum ResponseCode {

    /**
     * 成功执行
     */
    SUCCESS,

    /**
     * 重复执行
     */
    DUPLICATED,

    /**
     * 参数非法
     */
    ILLEGAL_ARGUMENT,

    /**
     * 系统错误
     */
    SYSTEM_ERROR,

    /**
     * 业务错误
     */
    BIZ_ERROR;

}
