package cn.com.edtechhub.nftbase.response;

/**
 * 响应编码
 *
 * @author limou3434
 */
public enum ResponseCode {

    /**
     * 执行成功
     */
    SUCCESS,

    /**
     * 执行重复
     */
    DUPLICATED,

    // TODO：这里加一个执行失败的状态，替代下面的业务错误

    // TODO：这里加一个执行异常的状态，替代下面的业务错误

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
