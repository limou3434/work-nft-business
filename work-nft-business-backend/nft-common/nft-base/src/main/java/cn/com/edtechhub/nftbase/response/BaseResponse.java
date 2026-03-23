package cn.com.edtechhub.nftbase.response;

import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通用响应体
 * 这里需要做概念区分
 * - 异常则抛出 SystemException/BizException/RemoteCallException，内部包含一个成员为 ErrorCode errorCode 接口实例，保证所有实现接口的实例内可以获取 (异常编码 code, 异常信息 message)，而异常的父类中把异常编码填充进异常消息
 * - 响应则返回 SingleResponse/MultiResponse/PageResponse (响应布尔 success, 响应编码 responseCode, 响应信息 responseMessage, 响应数据 data 等)
 * - 报文则返回 Result，这样做会比较符合 HTTP 的规范... TODO：我觉得这里还可以再修改概念一下下
 *
 * @author limou3434
 */
@ToString
@Data
public class BaseResponse implements Serializable {

    /**
     * 响应布尔
     */
    private Boolean success; // TODO：考虑修改个名字

    /**
     * 响应编码
     */
    private String responseCode;

    /**
     * 响应信息
     */
    private String responseMessage;

    /**
     * 序列版本
     */
    @Serial
    private static final long serialVersionUID = 1L;

}
