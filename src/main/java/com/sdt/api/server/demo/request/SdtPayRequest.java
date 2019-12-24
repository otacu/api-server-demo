package com.sdt.api.server.demo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 刷新token请求
 */
@Data
public class SdtPayRequest implements Serializable {
    private String transationNo;

    private String name;

    private String idCard;

    private String amount;
}
