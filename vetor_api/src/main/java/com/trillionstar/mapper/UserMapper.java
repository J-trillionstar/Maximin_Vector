package com.trillionstar.mapper;

import com.trillionstar.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    List<User> queryByparams(Map<String,Object> map);
    int insertUser(User user);
    int updateUser(Map<String,Object> map);
    int deleteWechat(String openid);
}
