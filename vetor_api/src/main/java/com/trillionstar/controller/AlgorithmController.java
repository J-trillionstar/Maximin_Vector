package com.trillionstar.controller;

import com.trillionstar.entity.Candidate;
import com.trillionstar.entity.Join;
import com.trillionstar.entity.Vector;
import com.trillionstar.mapper.CandidateMapper;
import com.trillionstar.mapper.JoinMapper;
import com.trillionstar.mapper.VectorMapper;
import com.trillionstar.utils.Maximin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {

    @Resource
    JoinMapper joinMapper;
    @Resource
    CandidateMapper candidateMapper;
    @Resource
    VectorMapper vectorMapper;

    public Object[] getVecList(String vectorid){
        //1:0,2;3:0,1,2;2:0;
        //1:0,2  3:0,1,2  2:0
        List<Join> joinList = joinMapper.queryJoin(vectorid);
        List<List<int[]>> vec_list = new ArrayList<>();
        List<Float> weights = new ArrayList<>();
        for (Join join : joinList) {
            List<int[]> temp = new ArrayList<>();
            String one_vec = join.getJ_result();
            String[] split = one_vec.split(";");
            for (String s : split) {
                //1  0,2
                String one = s.split(":")[0];
                String[] more = s.split(":")[1].split(",");
                for (String s1 : more) {
                    int b[] = {Integer.parseInt(one),Integer.parseInt(s1)};
                    temp.add(b);
                }
            }
            vec_list.add(temp);
            weights.add(join.getJ_weight());
        }
        Object[] vec_w = {vec_list,weights};
        return vec_w;
    }

    @PostMapping("/maximin")
    public void maximin(@RequestParam("vectorid") String vectorid){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("c_vectorid",vectorid);
        List<Candidate> candidateList = candidateMapper.queryByParams(hashMap);
        String[] candidates = new String[candidateList.size()];
        for (int i = 0; i < candidateList.size(); i++) {
            candidates[i]= Integer.toString(i+1);
        }
        Object[] vec_w = getVecList(vectorid);
        Maximin maximin = new Maximin();
        Object[] res = maximin.enter((List<List<int[]>>) vec_w[0], (List<Float>) vec_w[1], candidates);
        List<Integer> NW = (List<Integer>) res[0];
        List<Integer> NL = (List<Integer>) res[1];
        String v_result_reference = (String) res[2];
        String result = "";
        result+="NW:";
        for (Integer integer : NW) {
            result +=Integer.toString(integer)+",";
        }
        result = result.substring(0, result.length() - 1);
        result+=";NL:";
        for (Integer integer : NL) {
            result += Integer.toString(integer)+",";
        }
        result = result.substring(0, result.length() - 1);
        Vector vector = new Vector();
        vector.setV_vectorid(vectorid);
        vector.setV_result(result);
        vector.setV_result_reference(v_result_reference);
        vectorMapper.updateVector(vector);
    }
}
