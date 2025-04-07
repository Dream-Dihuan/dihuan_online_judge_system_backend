package com.dihuan.common.utils.jsonWebTokenUtils;

import io.jsonwebtoken.Claims;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    @org.junit.jupiter.api.Test
    void createToken() {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setId(1L);
        tokenInfo.setUsername("dihuan_jwt_test");
        String token = JwtUtils.createToken(tokenInfo);
        System.out.println(token);
    }

    @org.junit.jupiter.api.Test
    void parseToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3NDY2MTk2OTMsInN1YiI6IkxPR0lOX0FETUlOSVNUUkFUT1IiLCJ0b2tlbkluZm8iOiJ7XCJpZFwiOjEsXCJ1c2VybmFtZVwiOlwiZGlodWFuX2p3dF90ZXN0XCJ9In0.lM5m2nnR1LAiCA7xEEVEO5FIDhsYweDWoinQulKueBI";
        Claims claims = JwtUtils.parseToken(token);
        TokenInfo tokenInfo = JwtUtils.getTokenInfo(claims);
        System.out.println(tokenInfo);
    }
}