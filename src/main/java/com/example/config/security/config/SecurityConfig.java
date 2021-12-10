package com.example.config.security.config;

import com.example.config.security.filter.JwtAuthenticationTokenFilter;
import com.example.config.security.filter.MyAuthenticationFilter;
import com.example.config.security.handler.MyAccessDeniedHandler;
import com.example.config.security.service.UserServiceImpl;
import com.example.config.security.utils.JwtUtils;
import com.example.config.security.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author chen
 * @version 1.0
 * @date 2021/12/10 11:00
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    // 用于密码加解密，如果你需要自定义用户则需要提供一个 PasswordEncoder
    // 在身份校验时 Spring Security 会使用它来对密码进行解密
    // 在获取用户密码时，密码必须是加密状态的，所以在用户注册时也需要使用 PasswordEncoder 进行加密
    // 否则 PasswordEncoder 向一个未加密的密码进行解密，会导致密码错误
    @Bean
    public PasswordEncoder pw(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterAfter(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 所有请求都需要身份验证，关闭 CSRF
        http.authorizeRequests()
                //放行
                .antMatchers("/login").permitAll()
                //什么路径需要权限  严格区分大小写
//                .antMatchers("/toMain").hasAuthority("admin")
                //什么路径需要权限  严格区分大小写
//                .antMatchers("/toMain").hasAnyRole("user")
                //所有的请求都必须被验证（登录）
                .anyRequest().authenticated()
                //自定义access代替  .anyRequest().authenticated()  根据当前访问的url 判断用户里有没有权限，没有的话返回为false 不能访问
                // 可以做路径访问控制，也就是谁可以访问哪个路径
//                .anyRequest().access("@myAccessServiceImpl.hasPermission(request,authentication)")
                .and()
                .csrf().disable()
                .sessionManagement()                                         // 定制 session 策略
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);     // 调整为让 Spring Security 不创建和

        //退出登录
        http.logout()
                //自定义退出登录url
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");


        // 创建 AuthenticationFilter 实例
        UsernamePasswordAuthenticationFilter authenticationFilter =
                new MyAuthenticationFilter();

        //403自定义异常处理
        http.exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler());

        // 配置认证异常处理
        // 因为 AuthenticationEntryPoint 是函数式接口（只有一个方法的接口），
        // 所以我们可以使用 Lambda 表达式进行实现，之前的类可以删除了。
        // 如果不使用 Lambda 表达式，就直接传入一个实现类的实例既可。
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            // JSON 信息
            Map<String, Object> map = new HashMap<String, Object>(3);
            map.put("code", 401);
            map.put("message", "未登陆");
            map.put("success",false);
            map.put("data", authException.getMessage());

            // 将 JSON 信息写入响应
            ResponseUtil.send(response, map);
        });

        // 添加登陆 成功/失败 处理器  //
        // 因为这两个处理器也是函数式接口，所以这里同样使用 Lambda 表达式
        // 如果不使用 Lambda 表达式，就直接传入一个实现类的实例既可。
        authenticationFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
                String name = authentication.getName();
                Map<String, Object> map = new HashMap<String, Object>(4);
                Map<String, String> stringMap = new HashMap<>();
                JwtUtils jwtUtils = new JwtUtils();
                String token = jwtUtils.generateToken(name);
                stringMap.put("token",token);
                map.put("code", 200);
                map.put("msg", "登陆成功");
                map.put("success",true);
                map.put("data",stringMap );
                map.put("authentication",authentication);

                // 将 JSON 信息写入响应
                ResponseUtil.send(httpServletResponse, map);
            }
        });

        authenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            Map<String, Object> map = new HashMap<String, Object>(3);

            map.put("code", 401);
            if (exception instanceof LockedException){
                map.put("msg", "账户被锁定");
            }else if (exception instanceof CredentialsExpiredException){
                map.put("msg", "密码过期");
            }else if (exception instanceof AccountExpiredException){
                map.put("msg", "账户过期");
            }else if (exception instanceof DisabledException){
                map.put("msg", "账户被禁用");
            }else if (exception instanceof BadCredentialsException){
                map.put("msg", "用户名或者密码输入错误");
            }
            map.put("data", "");
            map.put("success",false);
            // 将 JSON 信息写入响应
            ResponseUtil.send(response, map);
        });




        // 配置 AuthenticationManager
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        // 替换过滤器
        http.addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
