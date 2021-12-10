package com.example.config.security.handler;

import com.example.config.security.utils.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
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
 * @date 2021/12/10 11:12
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> map = new HashMap<>();
        map.put("code", 403);
        map.put("message", "权限不足");
        map.put("success",false);
        map.put("data","");
        ResponseUtil.send(response,map);
    }
}
