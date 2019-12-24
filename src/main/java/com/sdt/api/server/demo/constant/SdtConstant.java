package com.sdt.api.server.demo.constant;

public final class SdtConstant {
    private SdtConstant () {

    }

    /**
     * 用于token的秘钥
     */
    public static final String JWT_SECRET_FOR_SIGN = "jwt_secret_for_sign";

    /**
     * token签发者
     */
    public static final String JWT_ISSUER = "顺德汽运";

    /**
     * accessToken 过期天数
     */
    public static final int ACCESS_TOKEN_EXPIRES_DAYS = 7;

}
