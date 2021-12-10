package com.example.pojo.entity;

import java.io.Serializable;

/**
 * (UserDate)实体类
 *
 * @author makejava
 * @since 2021-12-10 10:52:21
 */
public class UserDate implements Serializable {
    private static final long serialVersionUID = 540315898623510815L;
    

    
    private String userid;
    /**
    * 名字
    */
    private String name;
    
    private String email;
    /**
    * 状态
    */
    private String status;
    
    private String updatetime;
    
    private String createtime;




    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

}