package com.example.config.security.filter;

import com.example.config.security.service.UserServiceImpl;
import com.example.config.security.utils.JwtUtils;
import com.example.config.security.utils.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
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
 * @date 2021/12/10 15:32
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserServiceImpl userDetailsService;
    @Autowired
    private JwtUtils jwtUtils;

    private String tokenHeader = "token";

    private String tokenHead = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //拿到requset中的head
        String authHeader = request.getHeader(this.tokenHeader);
        try {
            if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
                // The part after "Bearer "
                String authToken = authHeader.substring(this.tokenHead.length());
                //解析token获取用户名
                String username = jwtUtils.getUserNameFromToken(authToken);
                log.info("checking username:{}", username);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    //正常的逻辑返回
                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        log.info("authenticated user:{}", username);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            // JSON 信息
            Map<String, Object> map = new HashMap<String, Object>(3);
            map.put("code", 201);
            map.put("message", "token 已过期");
            map.put("success",false);
            map.put("data", "");

            // 将 JSON 信息写入响应
            ResponseUtil.send(response, map);
        }catch (MalformedJwtException e){
            // JSON 信息
            Map<String, Object> map = new HashMap<String, Object>(3);
            map.put("code", 201);
            map.put("message", "传输的是一个错误的 token");
            map.put("success",false);
            map.put("data", "");

            // 将 JSON 信息写入响应
            ResponseUtil.send(response, map);
        }
        filterChain.doFilter(request, response);
    }
}
