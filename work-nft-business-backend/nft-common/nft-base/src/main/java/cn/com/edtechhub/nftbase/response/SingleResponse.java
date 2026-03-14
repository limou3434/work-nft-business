package cn.com.edtechhub.nftbase.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 单一响应
 *
 * @author limou3434
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SingleResponse<T> extends BaseResponse {

    /**
     * 单一数据
     */
    private T data;

    /**
     * 序列版本
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应构造方法
     *
     * @param data
     * @return
     * @param <T>
     */
    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setSuccess(true);
        singleResponse.setData(data);
        return singleResponse;
    }

}
