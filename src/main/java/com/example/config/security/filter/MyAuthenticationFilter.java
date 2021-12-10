package com.example.config.security.filter;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author chen
 * @version 1.0
 * @date 2021/12/10 11:34
 */
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //如果不是post请求则报错
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        // 判断 ContentType 类型  是否是json类型  如果不是则是表单登录
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {

            // 获取请求内容
            Map<String, String> loginData = new HashMap<>(2);

            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //拿到参数名称 getUsernameParameter  可以修改   request.getParameter("username"); 也可以拿到数据

            String username = loginData.get(getUsernameParameter());
            String password = loginData.get(getPasswordParameter());
            //
            String md5password = SecureUtil.md5(password);

            // 创建 Authentication
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, md5password);

            setDetails(request, authentication);



            return this.getAuthenticationManager().authenticate(authentication);
        }else {
            // 兼容表单登陆
            return super.attemptAuthentication(request, response);
        }

    }

}
