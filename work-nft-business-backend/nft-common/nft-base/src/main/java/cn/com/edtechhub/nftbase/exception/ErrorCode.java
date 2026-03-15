package cn.com.edtechhub.nftbase.exception;

/**
 * 错误码接口实现，所有不同类型的错误码枚举类必须实现这个接口（is-a 用继承、has-a 用组合、标化动作用接口）
 *
 * @author limou3434
 */
public interface ErrorCode {

    /**
     * 获取错误编码
     *
     * @return 错误编码
     */
    String getCode();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getMessage();

}
