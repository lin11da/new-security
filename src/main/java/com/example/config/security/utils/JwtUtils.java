package com.example.config.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * TODO
 *
 * @author chen
 * @version 1.0
 * @date 2021/12/10 15:10
 */
@Component
public class JwtUtils {

    private String secret = "**))&^%fFDKKL";

    private  Long expiration = 30L;


    /**
     * 创建token
     * @param username 用户名
     * @return
     */
    public  String generateToken(String username) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();

    }


    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public  String getUserNameFromToken(String token){
        return getTokenBody(token).getSubject();
    }



    /**
     *  是否已过期
     * @param token
     * @return
     */
    public  boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
