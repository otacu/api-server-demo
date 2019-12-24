package com.sdt.api.server.demo.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * appKey和appSecrect关系应该存redis，这里只是模拟
 */
public final class SdtAppSecrectEnum {
    private SdtAppSecrectEnum() {

    }

    /**
     * 定义Map
     */
    private static Map<String, String> map = new LinkedHashMap<String, String>();

    /**
     * 美的appKey
     */
    public static final String MIDEA = "midea_appkey";
    ;

    /**
     * 万和appKey
     */
    public static final String VANWARD = "vanward_appkey";

    static {
        map = new LinkedHashMap<String, String>();
        map.put(MIDEA, "midea_appsecrect");
        map.put(VANWARD, "vanward_appsecrect");
    }

    /**
     * 得到AppSecrect
     *
     * @param appKey 传入appKey
     * @return String
     */
    public static String getAppSecrect(String appKey) {
        return map.get(appKey);
    }

}
