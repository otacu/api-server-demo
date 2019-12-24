package com.sdt.api.server.demo.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SdtAccessToken implements Serializable {
    private String accessToken;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date expiresTime;
}
