package com.trillionstar.entity;

import lombok.Data;

import java.util.List;

@Data
public class Candidate {
    private String c_candidateid;
    private String c_name;
    private String c_introduce;
    private String c_imageurl;
    private String c_vectorid;
    private List<String> imgUrlList;
}
