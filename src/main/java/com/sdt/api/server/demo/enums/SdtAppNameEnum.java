package com.sdt.api.server.demo.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SdtAppNameEnum {
    private SdtAppNameEnum() {

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
        map.put(MIDEA, "美的");
        map.put(VANWARD, "万和");
    }

    /**
     * 得到name
     *
     * @param appKey 传入appKey
     * @return String
     */
    public static String getName(String appKey) {
        return map.get(appKey);
    }
}
