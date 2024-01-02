package com.trillionstar.mapper;

import com.trillionstar.entity.Vector;
import com.trillionstar.view.People;
import com.trillionstar.view.VectorCover;

import java.util.List;
import java.util.Map;

public interface VectorMapper {
    List<Vector> queryByParams(Map<String,Object> map);
    int updateOne(Map<String,Object> map);
    int insertVector(Vector vector);
    int updateVector(Vector vector);
    int updateVectorStatus(Map<String,Object> map);
    List<Vector> queryOwner(Map<String,Object> map);
    List<Vector> queryJoin(String j_userid,boolean isjoined);
    List<People> queryJoinPeos(String v_vectorid);
    List<VectorCover> queryCover(Map<String,Object> map);
    int changeBin(String vectorid,boolean v_isbin);
}
