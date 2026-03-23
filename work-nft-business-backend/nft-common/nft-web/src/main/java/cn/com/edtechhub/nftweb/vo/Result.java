package cn.com.edtechhub.nftweb.vo;

import cn.com.edtechhub.nftbase.response.ResponseCode;
import cn.com.edtechhub.nftbase.response.SingleResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * HTTP 报文中的载荷类 TODO：我还是无法理解为什么需要自己包一层载荷，只能勉强解释为为了分层概念
 *
 * @author limou3434
 */
@Getter
@Setter
public class Result<T> {

    /**
     * 是否成功标识
     */
    private Boolean success;

    /**
     * 自定义状态码
     */
    private String code;

    /**
     * 详细消息描述
     */
    private String message;

    /**
     * 可用视图数据
     */
    private T data;

    /**
     * 空构造器
     * TODO：后续考虑是否可以使用 Lombok 的 @NoArgsConstructor
     */
    public Result() {
    }

    /**
     * 自定义构造器
     *
     * @param success
     * @param code
     * @param message
     * @param data
     */
    public Result(Boolean success, String code, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    // TODO：这个方法真的需要么？
    /**
     * 自定义构造器
     *
     * @param success
     * @param message
     * @param data
     */
    public Result(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 自定义构造器
     *
     * @param singleResponse
     */
    public Result(SingleResponse<T> singleResponse) {
        this.success = singleResponse.getSuccess();
        this.data = singleResponse.getData();
        this.code = singleResponse.getResponseCode();
        this.message = singleResponse.getResponseMessage();
    }

    /**
     * 成功载荷
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(
                true,
                ResponseCode.SUCCESS.name(),
                ResponseCode.SUCCESS.name(),
                data
        );
    }

    /**
     * 失败载荷
     *
     * @param errorCode
     * @param errorMsg
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(String errorCode, String errorMsg) {
        return new Result<>(
                false,
                errorCode,
                errorMsg,
                null
        );
    }

}
