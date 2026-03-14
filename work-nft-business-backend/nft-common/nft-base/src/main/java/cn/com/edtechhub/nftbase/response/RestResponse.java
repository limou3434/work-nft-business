package cn.com.edtechhub.nftbase.response;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * RESTful 通用结构响应
 *
 * @author limou3434
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RestResponse extends BaseResponse {

    /**
     * 成功响应
     */
    private JSONObject data;

    /**
     * 失败响应
     */
    private JSONObject error;

    @Override
    public Boolean getSuccess() {
        return data != null;
    }

    @Override
    public String getResponseMessage() {
        if (this.error != null) {
            return error.getString("message");
        }
        return null;
    }

    @Override
    public String getResponseCode() {
        if (this.error != null) {
            return error.getString("code");
        }
        return null;
    }

}
