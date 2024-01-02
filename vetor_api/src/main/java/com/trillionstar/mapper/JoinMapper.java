package com.trillionstar.mapper;

import com.trillionstar.entity.Join;

import java.util.List;

public interface JoinMapper {
    int insertJoin(Join join);
    int updateRes(Join join);
    int updateWei(Join join);
    List<Join> isJoined(String j_userid,String j_vectorid);
    List<Join> queryJoin(String j_vectorid);
}
