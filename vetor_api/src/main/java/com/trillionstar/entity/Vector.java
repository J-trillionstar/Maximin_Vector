package com.trillionstar.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Vector {
    private String v_vectorid;
    private String v_name;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date v_endtime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date v_updatetime;
    private String v_description;
    private boolean v_ispublic;
    private boolean v_islive;
    private boolean v_isanyo;
    private boolean v_isbin;
    //1：草稿，2：发布，3.处理 4：完毕
    private int v_status;

    private String v_result;
    private String v_result_reference;

    private String v_imgurl;
    private List<Candidate> candidates;
}
