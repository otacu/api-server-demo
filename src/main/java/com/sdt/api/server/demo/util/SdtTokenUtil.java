package com.sdt.api.server.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sdt.api.server.demo.constant.SdtConstant;
import com.sdt.api.server.demo.pojo.SdtAccessToken;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public final class SdtTokenUtil {
    private SdtTokenUtil(){

    }

    public static SdtAccessToken getAccessToken(String audience) {
        String token="";
        Date now = new Date();
        Calendar expiresCal = Calendar.getInstance();
        expiresCal.setTime(now);
        expiresCal.add(Calendar.DAY_OF_YEAR, SdtConstant.ACCESS_TOKEN_EXPIRES_DAYS);
        token= JWT.create().withAudience(audience).withIssuer(SdtConstant.JWT_ISSUER).withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(now).withExpiresAt(expiresCal.getTime())
                .sign(Algorithm.HMAC256(SdtConstant.JWT_SECRET_FOR_SIGN));
        SdtAccessToken sdtAccessToken = new SdtAccessToken();
        sdtAccessToken.setAccessToken(token);
        sdtAccessToken.setExpiresTime(expiresCal.getTime());
        return sdtAccessToken;
    }
}
