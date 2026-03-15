package cn.com.edtechhub.nftbase.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用响应
 *
 * @author limou3434
 */
@ToString
@Data
public class BaseResponse implements Serializable {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 响应编码
     */
    private String responseCode;

    /**
     * 响应消息
     */
    private String responseMessage;

    /**
     * 序列版本
     */
    @Serial
    private static final long serialVersionUID = 1L;

}
