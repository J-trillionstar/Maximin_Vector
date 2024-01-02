package com.trillionstar.controller;

import com.alibaba.fastjson.JSON;
import com.trillionstar.entity.User;
import com.trillionstar.mapper.UserMapper;
import com.trillionstar.utils.UserUtil;
import com.trillionstar.utils.constant;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private UserMapper userMapper;

    @PostMapping("/registerOrLogin")
    public User registerOrLogin(@RequestParam("phone") String phone, @RequestParam("password") String password) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("u_phone", phone);
        List<User> users = userMapper.queryByparams(hashMap);
        Date date = new Date();
        if (users.isEmpty()) {
            User user = new User();
            user.setU_username(phone);
            user.setU_phone(phone);
            user.setU_password(password);
            user.setU_userid(UserUtil.get12UUID());
            user.setU_createat(date);
            user.setU_updateat(date);
            user.setU_loginat(date);
            userMapper.insertUser(user);
            return user;
        } else {
            hashMap.put("u_loginat", date);
            userMapper.updateUser(hashMap);
            return users.get(0);
        }
    }

    @PostMapping("/wechatLogin")
    public User wechatLogin(@RequestParam("code") String code,@RequestParam("username") String username,@RequestParam("imageurl") String imageurl) {
        String res = UserUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session?appid=" + constant.APPID + "&secret=" + constant.SECRET + "&js_code=" + code + "&grant_type=authorization_code");
        Map<String, String> map = (Map)JSON.parse(res);
        String openId = map.get("openid");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("u_openid", openId);
        List<User> users = userMapper.queryByparams(hashMap);
        Date date = new Date();
        if (users.isEmpty()){
            User user = new User();
            user.setU_userid(UserUtil.get12UUID());
            user.setU_username(username);
            user.setU_openid(openId);
            user.setU_createat(date);
            user.setU_updateat(date);
            user.setU_loginat(date);
            user.setU_imageurl(imageurl);
            userMapper.insertUser(user);
            return user;
        }else{
            hashMap.put("u_loginat",date);
            userMapper.updateUser(hashMap);
            return users.get(0);
        }
    }

}
