package com.example.pojo.entity;

import java.io.Serializable;

/**
 * (UserRole)实体类
 *
 * @author makejava
 * @since 2021-12-10 10:52:21
 */
public class UserRole implements Serializable {
    private static final long serialVersionUID = 328661849352096441L;
    

    /**
    * 用户id
    */
    private String userid;
    /**
    * 用户权限
    */
    private String userrole;
    
    private String updatime;
    
    private String createtime;




    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }

    public String getUpdatime() {
        return updatime;
    }

    public void setUpdatime(String updatime) {
        this.updatime = updatime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

}