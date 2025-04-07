package com.dihuan.common.utils.jsonWebTokenUtils;

import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.ResultCodeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {

    // token 的 秘钥
    private static SecretKey secretKey = Keys.hmacShaKeyFor("TheProgramIsDevelopedByTangHaowen".getBytes());
    private static final ObjectMapper objectMapper = new ObjectMapper();


    //生成 token
    public static String createToken(TokenInfo tokenInfo){

        try {
            String tokenInfoJson = objectMapper.writeValueAsString(tokenInfo);
            String token = Jwts.builder()
                    .setExpiration(new Date(System.currentTimeMillis()+60*1000*60*24*30L))
                    .setSubject("LOGIN_ADMINISTRATOR")
                    .claim("tokenInfo",tokenInfoJson)
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static Claims parseToken(String token){
        // 是否能够获取到token
        if (token == null){
            throw new DihuanException(ResultCodeEnum.FAIL,"未登录");
        }

        try{
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build();
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            return claimsJws.getBody();
        }catch (ExpiredJwtException e){
            throw new DihuanException(ResultCodeEnum.FAIL,"登录信息已过期");
        }catch (JwtException e){
            throw new DihuanException(ResultCodeEnum.FAIL,"非法登录信息");
        }
    }

    public static TokenInfo getTokenInfo(Claims claims) {
        try {
            String tokenInfoJson = claims.get("tokenInfo", String.class);
            return objectMapper.readValue(tokenInfoJson, TokenInfo.class);
        } catch (Exception e) {
            throw new DihuanException(ResultCodeEnum.FAIL, "无法解析用户信息");
        }
    }
}
