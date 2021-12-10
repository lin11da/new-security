package com.example.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2021-12-10 10:52:20
 */
@TableName("user")
@Data
public class User implements Serializable, UserDetails {
    private static final long serialVersionUID = -57437732543229635L;

    @TableField(exist = false)
    private UserDate userDate;
    
    private String userid;
    
    private String number;
    
    private String password;
    /**
    * 盐
    */
    private String salt;
    
    private String name;
    /**
    * 账户是否未过期
    */
    private Boolean isaccountnonexpired;
    /**
    * 账户是否未锁定
    */
    private Boolean isaccountnonlocked;
    /**
    * 账户凭证是否未过期
    */
    private Boolean iscredentialsnonexpired;
    /**
    * 账户是否启用
    */
    private Boolean isenabled;
    /**
    * 是否删除用户
    */
    private String udelete;
    
    private Date updatetime;
    
    private Date createtime;

    @TableField(exist=false)
    @JsonIgnore
    private List<GrantedAuthority> authorities;

    public List<String> getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }


    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return getIsaccountnonexpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getIsaccountnonlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getIscredentialsnonexpired();
    }

    @Override
    public boolean isEnabled() {
        return getIsenabled();
    }

    public User(UserDate userDate, List<GrantedAuthority> authorities) {
        this.userDate = userDate;
        this.authorities = authorities;
    }

    public User(){

    }




    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsaccountnonexpired() {
        return isaccountnonexpired;
    }

    public void setIsaccountnonexpired(Boolean isaccountnonexpired) {
        this.isaccountnonexpired = isaccountnonexpired;
    }

    public Boolean getIsaccountnonlocked() {
        return isaccountnonlocked;
    }

    public void setIsaccountnonlocked(Boolean isaccountnonlocked) {
        this.isaccountnonlocked = isaccountnonlocked;
    }

    public Boolean getIscredentialsnonexpired() {
        return iscredentialsnonexpired;
    }

    public void setIscredentialsnonexpired(Boolean iscredentialsnonexpired) {
        this.iscredentialsnonexpired = iscredentialsnonexpired;
    }

    public Boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(Boolean isenabled) {
        this.isenabled = isenabled;
    }

    public String getUdelete() {
        return udelete;
    }

    public void setUdelete(String udelete) {
        this.udelete = udelete;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

}