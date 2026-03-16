package cn.com.edtechhub.nftbase.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用请求体
 *
 * @author limou3434
 */
@Data
public class BaseRequest implements Serializable {

    /**
     * 序列版本
     */
    @Serial
    private static final long serialVersionUID = 1L;

}
