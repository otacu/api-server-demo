package com.sdt.api.server.demo.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sdt.api.server.demo.annotation.NeedToken;
import com.sdt.api.server.demo.annotation.NoNeedToken;
import com.sdt.api.server.demo.constant.SdtConstant;
import com.sdt.api.server.demo.constant.SdtResultStatusConstant;
import com.sdt.api.server.demo.request.SdtRequest;
import com.sdt.api.server.demo.response.SdtResult;
import com.sdt.api.server.demo.util.SdtSignUtil;
import com.sdt.api.server.demo.util.SdtStringUtil;
import com.sdt.api.server.demo.util.SdtValidatorUtil;
import com.sdt.api.server.demo.util.SdtWebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Calendar;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        SdtRequest request = SdtWebUtil.request2Bean(httpServletRequest, SdtRequest.class);
        log.debug(JSON.toJSONString(request));
        // 校验必填参数
        String errorMsg = SdtValidatorUtil.validatePojo(request);
        if (SdtStringUtil.isNotBlank(errorMsg)) {
            this.printResponse(httpServletResponse, new SdtResult(SdtResultStatusConstant.STATUS_401, errorMsg, null));
            return false;
        }
        // 验签， 验证数据是否有被篡改
        if (!SdtSignUtil.checkSign(request)) {
            this.printResponse(httpServletResponse, new SdtResult(SdtResultStatusConstant.STATUS_401, "验签失败", null));
            return false;
        }
        // 时间戳误差在10分钟以内
        if (!this.checkTimestamp(request.getTimestamp())) {
            this.printResponse(httpServletResponse, new SdtResult(SdtResultStatusConstant.STATUS_401, "时间戳验证失败", null));
            return false;
        }
        // TODO 可以考虑把nonce存进redis，过期时间10分钟，每次收到请求去redis匹配看是不是已经有，有就是重放攻击
        String accessToken = request.getAccessToken();
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有NoNeedToken注释，有则跳过认证
        if (method.isAnnotationPresent(NoNeedToken.class)) {
            NoNeedToken passToken = method.getAnnotation(NoNeedToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(NeedToken.class)) {
            NeedToken needToken = method.getAnnotation(NeedToken.class);
            if (needToken.required()) {
                // 执行认证
                if (accessToken == null) {
                    this.printResponse(httpServletResponse, new SdtResult(SdtResultStatusConstant.STATUS_401, "无accessToken", null));
                    return false;
                }

                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SdtConstant.JWT_SECRET_FOR_SIGN)).build();
                try {
                    jwtVerifier.verify(accessToken);
                } catch (JWTVerificationException e) {
                    this.printResponse(httpServletResponse, new SdtResult(SdtResultStatusConstant.STATUS_401, "accessToken验证失败或者过期", null));
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    private void printResponse(HttpServletResponse httpServletResponse, SdtResult sdtResult) throws Exception {
        //重置response
        httpServletResponse.reset();
        //设置编码格式
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = httpServletResponse.getWriter();
        pw.write(JSON.toJSONString(sdtResult));
        pw.flush();
        pw.close();
    }

    private boolean checkTimestamp(long requestTimestamp) {
        Calendar maxCal = Calendar.getInstance();
        maxCal.setTimeInMillis(requestTimestamp);
        maxCal.add(Calendar.MINUTE, 5);

        Calendar minCal = Calendar.getInstance();
        minCal.setTimeInMillis(requestTimestamp);
        minCal.add(Calendar.MINUTE, -5);

        long now = System.currentTimeMillis();
        return now <= maxCal.getTimeInMillis() && now >= minCal.getTimeInMillis();
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}