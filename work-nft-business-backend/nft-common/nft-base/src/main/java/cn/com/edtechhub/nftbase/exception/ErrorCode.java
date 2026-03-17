package cn.com.edtechhub.nftbase.exception;

/**
 * 作为异常代码的行为，所有不同类型的错误码枚举类必须实现这个接口（标化行为用接口、is-a 用继承、has-a 用组合）
 *
 * @author limou3434
 */
public interface ErrorCode {

    /**
     * 获取异常编码
     *
     * @return 异常编码
     */
    String getCode();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    String getMessage();

}
