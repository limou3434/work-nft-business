package cn.com.edtechhub.nftweb.vo;

import cn.com.edtechhub.nftbase.response.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author limou3434
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MultiResult<T> extends Result<List<T>> {

    // TODO：这里的字段含义感觉很奇怪，后续用的时候对照一下
    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页记录数
     */
    private int size;

    public MultiResult() {
        super();
    }

    public MultiResult(Boolean success, String code, String message, List<T> data, long total, int page, int size) {
        super(success, code, message, data);
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public static <T> MultiResult<T> successMulti(List<T> data, long total, int page, int size) {
        return new MultiResult<>(true, ResponseCode.SUCCESS.name(), ResponseCode.SUCCESS.name(), data, total, page, size);
    }

    public static <T> MultiResult<T> errorMulti(String errorCode, String errorMsg) {
        return new MultiResult<>(true, errorCode, errorMsg, null, 0, 0, 0);
    }

}
