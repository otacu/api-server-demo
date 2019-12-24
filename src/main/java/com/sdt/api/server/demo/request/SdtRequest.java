package com.sdt.api.server.demo.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 请求公共参数
 */
@Data
public class SdtRequest implements Serializable {

    /**
     * 分配给api用户的唯一标识
     */
    @NotBlank(message = "appKey不能为空")
    private String appKey;

    /**
     * api版本号
     */
    @NotBlank(message = "version不能为空")
    private String version;

    /**
     * 调用时间戳
     */
    @NotNull(message = "timestamp不能为空")
    private Long timestamp;

    /**
     * 随机数
     */
    @NotBlank(message = "nonce不能为空")
    private String nonce;

    /**
     * 签名
     */
    @NotBlank(message = "sign不能为空")
    private String sign;

    /**
     * accessToken
     */
    private String accessToken;

    /**
     * request
     */
    private String request;
}
