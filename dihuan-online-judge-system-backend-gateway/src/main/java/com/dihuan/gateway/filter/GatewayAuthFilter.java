package com.dihuan.gateway.filter;
import com.dihuan.common.localThread.TokenInfoHolder;
import com.dihuan.common.utils.jsonWebTokenUtils.JwtUtils;
import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class GatewayAuthFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    // 白名单路径（不需要认证的路径）
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/doc.html",
            "/**/api-docs",
            "/**"
    );

    // 白名单中的黑名单路径（虽然父路径在白名单中，但这些特定路径需要认证）
    private static final List<String> WHITELIST_BLACKLIST = Arrays.asList(
            "/api/user/getUser"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String path = serverHttpRequest.getURI().getPath();
        // 判断路径中是否包含 inner，只允许内部调用
//        if (antPathMatcher.match("/**/inner/**", path)) {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            DataBufferFactory dataBufferFactory = response.bufferFactory();
//            DataBuffer dataBuffer = dataBufferFactory.wrap("无权限".getBytes(StandardCharsets.UTF_8));
//            return response.writeWith(Mono.just(dataBuffer));
//        }

        // 2. 检查是否是白名单中的黑名单路径（需要认证）
        if (WHITELIST_BLACKLIST.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path))) {
            // 这些路径需要走正常的认证流程
            return loginAuthentication(exchange, chain);
        }

        // 3. 白名单路径直接放行
        if (WHITE_LIST.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path))){
            return chain.filter(exchange);
        }

        // todo 统一权限校验，通过 JWT 获取登录用户信息
        return loginAuthentication(exchange,chain);
    }

    private Mono<Void> loginAuthentication(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst("dihuan_oj_system_token");

        // 判断token是否有效
        Claims claims = JwtUtils.parseToken(token);

        TokenInfo tokenInfo = JwtUtils.getTokenInfo(claims);
        TokenInfoHolder.setLoginAdminUser(tokenInfo);

        return chain.filter(exchange);
    }


    /**
     * 优先级提到最高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
