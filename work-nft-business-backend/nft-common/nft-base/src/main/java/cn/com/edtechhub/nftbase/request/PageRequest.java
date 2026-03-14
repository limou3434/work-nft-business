package cn.com.edtechhub.nftbase.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 分页请求
 *
 * @author limou3434
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageRequest extends BaseRequest {

    /**
     * 当前页数
     */
    private int currentPage;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 序列版本
     */
    @Serial
    private static final long serialVersionUID = 1L;

}
