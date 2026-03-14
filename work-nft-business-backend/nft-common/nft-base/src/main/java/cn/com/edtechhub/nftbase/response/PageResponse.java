package cn.com.edtechhub.nftbase.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * 分页响应
 *
 * @author limou3434
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageResponse<T> extends MultiResponse<T> {

    /**
     * 当前页数
     */
    private int currentPage;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 总页面数
     */
    private int totalPage;

    /**
     * 总条目数
     */
    private int total;

    /**
     * 序列版本
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应构造方法
     *
     * @param datas
     * @param total
     * @param pageSize
     * @param currentPage
     * @return
     * @param <T>
     */
    public static <T> PageResponse<T> of(List<T> datas, int total, int pageSize,int currentPage) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setSuccess(true);
        pageResponse.setDatas(datas);
        pageResponse.setTotal(total);
        pageResponse.setPageSize(pageSize);
        pageResponse.setCurrentPage(currentPage);
        pageResponse.setTotalPage((pageSize + total - 1) / pageSize);
        return pageResponse;
    }

}
