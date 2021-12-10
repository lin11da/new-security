package com.example.config.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mapper.User_Plus;
import com.example.mapper.User_Role_Plus;
import com.example.pojo.entity.User;
import com.example.pojo.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author chen
 * @version 1.0
 * @date 2021/12/10 11:13
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    User_Plus user_plus;

    @Autowired
    User_Role_Plus user_role_plus;

    @Autowired
    PasswordEncoder pw;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number",username).eq("udelete","0");
        User user = user_plus.selectOne(queryWrapper);
        if (user == null ){
            throw new BadCredentialsException("用户名或密码错误");
        }

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("userid",user.getUserid());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<UserRole> list = Collections.unmodifiableList(user_role_plus.selectList(userRoleQueryWrapper));

        List<String> collect = list.stream().map(UserRole::getUserrole).collect(Collectors.toList());
        for (String authority : collect){
            if (!("").equals(authority) & authority !=null){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
                grantedAuthorities.add(grantedAuthority);
            }
        }
        String encode = pw.encode(user.getPassword());

        //将用户所拥有的权限加入GrantedAuthority集合中
        return new org.springframework.security.core.userdetails.User(user.getNumber(),
                encode,user.getIsenabled(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isAccountNonLocked(), grantedAuthorities);
    }
}
