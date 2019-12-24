package com.sdt.api.server.demo.util;

import com.sdt.api.server.demo.constant.SdtParamNameConstant;
import com.sdt.api.server.demo.enums.SdtAppSecrectEnum;
import com.sdt.api.server.demo.exception.SdtException;
import com.sdt.api.server.demo.request.SdtRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class SdtSignUtil {
    private SdtSignUtil(){

    }

    /**
     * 验签
     * @param request
     * @return
     * @throws SdtException
     */
    public static Boolean checkSign(SdtRequest request) throws SdtException {
        try {
            return request.getSign().equals(SdtSignUtil.sign(request));
        } catch (Exception e) {
            throw new SdtException("验签异常");
        }
    }

    /**
     * 签名
     * @param request
     * @return
     * @throws SdtException
     */
    public static String sign(SdtRequest request) throws SdtException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put(SdtParamNameConstant.TIMESTAMP, String.valueOf(request.getTimestamp()));
            params.put(SdtParamNameConstant.APP_KEY, request.getAppKey());
            params.put(SdtParamNameConstant.VERSION, request.getVersion());
            params.put(SdtParamNameConstant.NONCE, request.getNonce());
            if (SdtStringUtil.isNotBlank(request.getAccessToken())) {
                params.put(SdtParamNameConstant.ACCESS_TOKEN, request.getAccessToken());
            }
            String data = SdtSignUtil.convertToSortStr(params) + request.getRequest();
            return HmacUtil.byte2hex(HmacUtil.encryptHMAC(data, SdtAppSecrectEnum.getAppSecrect(request.getAppKey())));
        } catch (Exception e) {
            throw new SdtException("签名异常");
        }
    }

    /**
     * 将Map转化为排序后的组字符串
     *
     * @param params
     * @return
     */
    private static String convertToSortStr(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder query = new StringBuilder();

        for (String key : keys) {
            String value = params.get(key);
            if (isNotEmpty(key, value)) {
                query.append(key).append(value);
            }
        }

        return query.toString();
    }

    private static boolean isNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !StringUtils.isEmpty(value);
            }
        }
        return result;
    }
}
