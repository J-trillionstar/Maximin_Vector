package com.trillionstar.controller;


import com.alibaba.fastjson.JSON;
import com.trillionstar.entity.User;
import com.trillionstar.mapper.UserMapper;
import com.trillionstar.utils.UserUtil;
import com.trillionstar.utils.constant;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mine")
public class MineController {
    @Resource
    UserMapper userMapper;

    //@PostMapping("/getUserInfo")
    //public User getUserInfo(@RequestParam("uniqId") String uniqId, @RequestParam("judge") int judge) {
    //    HashMap<String, Object> hashMap = new HashMap<>();
    //    if (judge == 1) {
    //        hashMap.put("u_phone", uniqId);
    //    } else {
    //        hashMap.put("u_openid", uniqId);
    //    }
    //    List<User> users = userMapper.queryByparams(hashMap);
    //    return users.get(0);
    //}
    @PostMapping("/getUserInfo")
    public User getUserInfo(@RequestParam("userid") String userid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("u_userid",userid);
        List<User> users = userMapper.queryByparams(hashMap);
        return users.get(0);
    }

    @PostMapping("/wxBind")
    public void wxBind(@RequestParam("code") String code, @RequestParam("phone") String phone) {
        String res = UserUtil.sendGet("https://api.weixin.qq.com/sns/jscode2session?appid=" + constant.APPID + "&secret=" + constant.SECRET + "&js_code=" + code + "&grant_type=authorization_code");
        Map<String, String> map = (Map) JSON.parse(res);
        String openId = map.get("openid");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("u_openid", openId);
        List<User> users = userMapper.queryByparams(hashMap);
        Date date = new Date();
        if (!users.isEmpty()) {
            userMapper.deleteWechat(openId);
        }
        hashMap.put("u_phone", phone);
        hashMap.put("u_loginAt", date);
        userMapper.updateUser(hashMap);
    }

    //@PostMapping("/changeMes")
    //public void changeMes(HttpServletRequest request, @RequestParam("uniqid") String uniqid, @RequestParam("judge") int judge, @RequestParam("key") String key, @RequestParam("value") String value) {
    //    HashMap<String, Object> hashMap = new HashMap<>();
    //    if (judge == 1) {
    //        hashMap.put("u_phone", uniqid);
    //    } else {
    //        hashMap.put("u_openid", uniqid);
    //    }
    //    //若更新的是头像图片，则删除原先的图片
    //    if (key == "u_imageurl") {
    //        List<User> users = userMapper.queryByparams(hashMap);
    //        UserUtil.deleteFile(request.getServletContext().getRealPath("/") + users.get(0).getU_imageurl());
    //    }
    //    hashMap.put(key, value);
    //    userMapper.updateUser(hashMap);
    //}
    @PostMapping("/changeMes")
    public void changeMes(HttpServletRequest request, @RequestParam("userid") String userid, @RequestParam("key") String key, @RequestParam("value") String value) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("u_userid", userid);
        //若更新的是头像图片，则删除原先的图片
        if (key == "u_imageurl") {
            List<User> users = userMapper.queryByparams(hashMap);
            UserUtil.deleteFile(request.getServletContext().getRealPath("/") + users.get(0).getU_imageurl());
        }
        hashMap.put(key, value);
        userMapper.updateUser(hashMap);
    }

    //@PostMapping("/uploadImage")
    //public String uploadImage(HttpServletRequest request, @RequestParam("uniqid") String uniqid, @RequestParam("judge") int judge, @RequestParam("file") MultipartFile[] files) {
    //    if (files == null || (files.length == 1 && files[0].getSize() == 0)) {
    //        return "图片为空";
    //    } else {
    //        MultipartFile mfile = files[0];
    //        String fileName = mfile.getOriginalFilename();
    //        String servicePath = request.getServletContext().getRealPath("/");
    //        String filePath = uniqid + "/" + constant.IMAGE;
    //        System.out.println(filePath);
    //        File file = UserUtil.makeFile(servicePath+filePath, fileName);
    //        try {
    //            mfile.transferTo(file);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        changeMes(request, uniqid, judge, "u_imageurl", filePath + "/" + fileName);
    //        return filePath + "/" + fileName;
    //    }
    @PostMapping("/uploadImage")
    public String uploadImage(HttpServletRequest request, @RequestParam("userid") String userid, @RequestParam("file") MultipartFile[] files) {
        if (files == null || (files.length == 1 && files[0].getSize() == 0)) {
            return "图片为空";
        } else {
            MultipartFile mfile = files[0];
            String fileName = mfile.getOriginalFilename();
            String servicePath = request.getServletContext().getRealPath("/");
            String filePath = userid + "/" + constant.IMAGE;
            System.out.println(filePath);
            File file = UserUtil.makeFile(servicePath + filePath, fileName);
            try {
                mfile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            changeMes(request, userid, "u_imageurl", filePath + "/" + fileName);
            return filePath + "/" + fileName;
        }
    }
}
