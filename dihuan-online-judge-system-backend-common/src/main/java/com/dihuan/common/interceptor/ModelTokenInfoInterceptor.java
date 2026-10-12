package com.dihuan.common.interceptor;


import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.utils.jsonWebTokenUtils.JwtUtils;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;



@Component
public class ModelTokenInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 先清空，避免上一个请求残留的登录态被本请求误用（Tomcat 线程复用）
        TokenInfoHolder.clearTokenInfo();

        // 获取并解析token
        String token = request.getHeader("dihuan_oj_system_token");
        if(token!=null){
            Claims claims = JwtUtils.parseToken(token);

            //获取token中的属性值
            TokenInfo tokenInfo = JwtUtils.getTokenInfo(claims);

            //将属性存入ThreadLocal中
            TokenInfoHolder.setLoginAdminUser(tokenInfo);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束清理 ThreadLocal，防止线程复用导致串用户
        TokenInfoHolder.clearTokenInfo();
    }

}
