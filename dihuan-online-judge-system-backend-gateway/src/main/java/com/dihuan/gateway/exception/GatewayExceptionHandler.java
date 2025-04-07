package com.dihuan.gateway.exception;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.Result;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        // 设置响应头为 JSON 格式
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        // 构造返回结果
        Result result;
        if (ex instanceof DihuanException) {
            DihuanException dihuanException = (DihuanException) ex;
            result = Result.fail(dihuanException.getCode(), dihuanException.getMessage());
        }
        else {
            ex.printStackTrace();
            result = Result.fail();
        }

        // 将结果序列化为 JSON
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(result);
        } catch (Exception e) {
            return Mono.error(e);
        }

        // 写入响应体
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}