package cn.com.edtechhub.nftbase.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 多元响应
 *
 * @author limou3434
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MultiResponse<T> extends BaseResponse implements Serializable {

    /**
     * 多元数据
     */
    private List<T> datas;

    /**
     * 序列版本
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造成功响应方法
     *
     * @param datas
     * @return
     * @param <T>
     */
    public static <T> MultiResponse<T> of(List<T> datas) {
        MultiResponse<T> multiResponse = new MultiResponse<>();
        multiResponse.setSuccess(true);
        multiResponse.setDatas(datas);
        return multiResponse;
    }

}
