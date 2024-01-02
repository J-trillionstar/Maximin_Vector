package com.trillionstar.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trillionstar.entity.*;
import com.trillionstar.entity.Vector;
import com.trillionstar.mapper.*;
import com.trillionstar.utils.UserUtil;
import com.trillionstar.utils.constant;
import com.trillionstar.view.People;
import com.trillionstar.view.VectorCover;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@RestController
@RequestMapping("/vector")
public class VectorController {
    @Resource
    VectorMapper vectorMapper;
    @Resource
    CandidateMapper candidateMapper;
    @Resource
    OwnerMapper ownerMapper;
    @Resource
    ImageMapper imageMapper;
    @Resource
    JoinMapper joinMapper;

    @PostMapping("/draftOrPublish")
    public String draftOrPublish(HttpServletRequest request, @RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        //System.out.println(json);
        Vector vector = jsonObject.getObject("vector", Vector.class);
        String vectorid = vector.getV_vectorid();
        vector.setV_updatetime(new Date());
        //System.out.println(vector);

        int status = jsonObject.getObject("status", Integer.class);
        vector.setV_status(status);

        if (vectorid.isEmpty()) {
            vectorid = UserUtil.get12UUID();
            vector.setV_vectorid(vectorid);
            vectorMapper.insertVector(vector);

            String uid = jsonObject.getObject("userid", String.class);
            Owner owner = new Owner();
            owner.setO_userid(uid);
            owner.setO_vetorid(vectorid);
            ownerMapper.insertOwner(owner);
        } else {
            vectorMapper.updateVector(vector);
        }

        HashMap<String, Object> c_map = new HashMap<>();
        c_map.put("c_vectorid", vectorid);
        Candidate[] candidates = jsonObject.getObject("candidates", Candidate[].class);
        List<Candidate> candidateList = Arrays.asList(candidates);
        List<String> new_cids = new ArrayList<>();
        List<String> db_cids = candidateMapper.queryIdByParams(c_map);
        for (Candidate can : candidateList) {
            String cid = UserUtil.toPinyin(can.getC_name());
            new_cids.add(cid);
            can.setC_imageurl(constant.VECTOR + "/" + vectorid + "/" + cid);
            can.setC_vectorid(vectorid);
            can.setC_candidateid(cid);
            if (db_cids.contains(UserUtil.toPinyin(can.getC_name()))) { //该候选人存在
                candidateMapper.updateCandidate(can);
            } else {
                candidateMapper.insertOneCandidate(can);
            }
            //------------------------------------------
            imageMapper.delImage(can.getC_imageurl());
        }
        for (String db_cid : db_cids) {
            if (!new_cids.contains(db_cid)) {
                HashMap<String, Object> h_m = new HashMap<>();
                h_m.put("c_vectorid", vectorid);
                h_m.put("c_candidateid", db_cid);
                candidateMapper.deleteCandidate(h_m);
            }
        }
        //UserUtil.deleteFileOrDirectory(request.getServletContext().getRealPath("/") + constant.VECTOR +"/"+ vectorid);
        return vectorid;
    }

    //上传投票封面
    @PostMapping("uploadVecImg")
    public String uploadVecImg(HttpServletRequest request, @RequestParam("vectorid") String vectorid, @RequestParam("file") MultipartFile[] files) {
        if (files == null || (files.length == 1 && files[0].getSize() == 0)) {
            return "图片为空";
        } else {
            MultipartFile mfile = files[0];
            String fileName = mfile.getOriginalFilename();
            String servicePath = request.getServletContext().getRealPath("/");
            String filePath = constant.VECTOR + "/" + vectorid;
            System.out.println(filePath);
            File file = UserUtil.makeFile(servicePath + filePath, fileName);
            try {
                mfile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imgurl = filePath + "/" + fileName;
            HashMap<String, Object> hmap = new HashMap<>();
            hmap.put("v_vectorid", vectorid);
            hmap.put("v_imgurl", imgurl);
            vectorMapper.updateOne(hmap);
            return filePath + "/" + fileName;
        }
    }

    //上传候选人图片
    @PostMapping("uploadCanImg")
    public String uploadCanImg(HttpServletRequest request, @RequestParam("vectorid") String vectorid, @RequestParam("c_name") String c_name, @RequestParam("file") MultipartFile[] files) {
        if (files == null || (files.length == 1 && files[0].getSize() == 0)) {
            return "图片为空";
        } else {
            MultipartFile mfile = files[0];
            String fileName = mfile.getOriginalFilename();
            String servicePath = request.getServletContext().getRealPath("/");
            String filePath = constant.VECTOR + "/" + vectorid + "/" + UserUtil.toPinyin(c_name);
            System.out.println(filePath);
            File file = UserUtil.makeFile(servicePath + filePath, fileName);
            try {
                mfile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image image = new Image();
            image.setI_imageurl(filePath);
            image.setI_imgpath(filePath + "/" + fileName);
            imageMapper.insertImage(image);
            return filePath + "/" + fileName;
        }
    }

    @PostMapping("/queryByParams")
    public Vector queryByParams(@RequestParam("key") String key, @RequestParam("value") String value) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key, value);
        List<Vector> vectorList = vectorMapper.queryByParams(hashMap);
        return vectorList.get(0);
    }

    @PostMapping("/changeStatus")
    public Vector changeStatus(@RequestParam("vectorid") String vectorid, @RequestParam("status") String status) {
        HashMap<String, Object> v_hmap = new HashMap<>();
        v_hmap.put("v_status", status);
        v_hmap.put("v_updatetime", new Date());
        v_hmap.put("v_vectorid", vectorid);
        vectorMapper.updateVectorStatus(v_hmap);

        HashMap<String, Object> hmap = new HashMap<>();
        hmap.put("v_vectorid", vectorid);
        return vectorMapper.queryByParams(hmap).get(0);
    }

    @PostMapping("/queryOwner")
    public List<Vector> queryOwner(@RequestParam("userid") String userid, @RequestParam("key") String key, @RequestParam("value") String value) {
        HashMap<String, Object> v_hmap = new HashMap<>();
        v_hmap.put(key, value);
        v_hmap.put("o_userid", userid);
        List<Vector> vectorList = vectorMapper.queryOwner(v_hmap);
        return vectorList;
    }

    @PostMapping("queryCandidates")
    public List<Candidate> queryCandidates(HttpServletRequest request, @RequestParam("vectorid") String vectorid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("c_vectorid", vectorid);
        List<Candidate> candidateList = candidateMapper.queryByParams(hashMap);
        for (Candidate candidate : candidateList) {
            //List<String> imgUrlList = UserUtil.getFileNames(request.getServletContext().getRealPath("/")+candidate.getC_imageurl(),candidate.getC_imageurl());
            System.out.println(candidate.getC_imageurl());
            List<String> imgUrlList = imageMapper.queryImagePath(candidate.getC_imageurl());
            candidate.setImgUrlList(imgUrlList);
        }
        return candidateList;
    }

    @PostMapping("/queryJoinMy")
    public List<Vector> queryJoinMy(@RequestParam("userid") String userid, @RequestParam("isjoined") boolean isjoined) {
        return vectorMapper.queryJoin(userid, isjoined);
    }

    @PostMapping("/queryJoinPeos")
    public List<People> queryJoinPeos(@RequestParam("vectorid") String vectorid) {
        List<People> peoples = vectorMapper.queryJoinPeos(vectorid);
        System.out.println(peoples);
        return peoples;
    }

    @PostMapping("/submitResult")
    public String submitResult(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String vectorid = jsonObject.getString("vectorid");
        String userid = jsonObject.getString("userid");
        boolean fromsquare = jsonObject.getBoolean("fromsquare");
        Result[] res = jsonObject.getObject("result", Result[].class);
        String str_res = "";
        for (Result re : res) {
            str_res = str_res + re.getOne()[0] + ":";
            for (String s : re.getMore()) {
                str_res = str_res + s + ",";
            }
            str_res = str_res.substring(0, str_res.length() - 1);
            str_res = str_res + ";";
        }
        System.out.println(str_res);
        Join join = new Join();
        join.setJ_userid(userid);
        join.setJ_vectorid(vectorid);
        join.setJ_result(str_res);
        if (fromsquare) {
            join.setJ_weight(1);
            joinMapper.insertJoin(join);
        } else {
            joinMapper.updateRes(join);
        }
        return "OK";
    }

    @PostMapping("/submitWeight")
    public String submitWeight(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String vectorid = jsonObject.getString("vectorid");
        People[] peoples = jsonObject.getObject("peoples", People[].class);
        for (People people : peoples) {
            Join join = new Join();
            join.setJ_userid(people.getU_userid());
            join.setJ_vectorid(vectorid);
            float temp = people.getJ_weight();
            join.setJ_weight(temp);
            if (temp != 1) {
                joinMapper.updateWei(join);
            }
        }
        return "OK";
    }

    @PostMapping("/queryCover")
    public List<VectorCover> queryCover(@RequestParam("order") String order) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(order,order);
        List<VectorCover> vectorCovers = vectorMapper.queryCover(hashMap);
        return vectorCovers;
    }

    @PostMapping("/isJoined")
    public List isJoined(@RequestParam("userid") String userid,@RequestParam("vectorid") String vectorid){
        return joinMapper.isJoined(userid,vectorid);
    }

    @PostMapping("/changeBin")
    public String changeBin(@RequestParam("vectorid") String vectorid,@RequestParam("v_isbin") boolean v_isbin){
        vectorMapper.changeBin(vectorid,v_isbin);
        return "OK";
    }

    @PostMapping("/insertJoin")
    public String insertJoin(@RequestParam("userid") String userid,@RequestParam("vectorid") String vectorid){
        Join join = new Join();
        join.setJ_userid(userid);
        join.setJ_vectorid(vectorid);
        join.setJ_weight(1);
        join.setJ_result("99");
        try{
        joinMapper.insertJoin(join);
        }catch(Exception e){
            return "isExist";
        }
        return "OK";
    }
}
