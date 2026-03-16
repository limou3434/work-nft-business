package cn.com.edtechhub.nftbase.exception;

/**
 * 异常代码接口，所有不同类型的错误码枚举类必须实现这个接口（is-a 用继承、has-a 用组合、标化动作用接口）
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
