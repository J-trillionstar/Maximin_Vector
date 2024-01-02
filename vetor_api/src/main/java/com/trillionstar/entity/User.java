package com.trillionstar.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String u_userid;
    private String u_username;
    private String u_imageurl;
    private String u_password;
    private String u_openid;
    private Date u_createat;
    private Date u_updateat;
    private String u_phone;
    private Date u_loginat;
}
