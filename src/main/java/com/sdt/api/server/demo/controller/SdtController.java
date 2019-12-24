package com.sdt.api.server.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.sdt.api.server.demo.annotation.NeedToken;
import com.sdt.api.server.demo.constant.SdtResultStatusConstant;
import com.sdt.api.server.demo.enums.SdtAppNameEnum;
import com.sdt.api.server.demo.pojo.SdtAccessToken;
import com.sdt.api.server.demo.request.SdtPayRequest;
import com.sdt.api.server.demo.request.SdtRequest;
import com.sdt.api.server.demo.response.SdtResult;
import com.sdt.api.server.demo.util.SdtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SdtController {
    /**
     * @param request 请求
     * @return 结果
     */
    @PostMapping("/auth/getAccessToken")
    @ResponseBody
    public SdtResult getAccessToken(SdtRequest request) {
        try {
            SdtAccessToken sdtAccessToken = SdtTokenUtil.getAccessToken(SdtAppNameEnum.getName(request.getAppKey()));
            return SdtResult.ok(sdtAccessToken);
        } catch (Exception e) {
            log.error("获取accessToken异常", e);
            return new SdtResult(SdtResultStatusConstant.STATUS_500, "获取accessToken异常", null);
        }
    }

    /**
     * @param request 请求
     * @return 结果
     */
    @NeedToken
    @PostMapping("/business/pay")
    @ResponseBody
    public SdtResult pay(SdtRequest request) {
        try {
            SdtPayRequest sdtPayRequest = JSONObject.parseObject(request.getRequest(), SdtPayRequest.class);
            //TODO 支付业务
            return new SdtResult(SdtResultStatusConstant.STATUS_200, "支付成功", null);
        } catch (Exception e) {
            log.error("支付异常", e);
            return new SdtResult(SdtResultStatusConstant.STATUS_500, "支付异常", null);
        }
    }
}
