package com.trillionstar.mapper;

import com.trillionstar.entity.Candidate;

import java.util.List;
import java.util.Map;

public interface CandidateMapper {
    int insertCandidate(List<Candidate> candidateList);
    int insertOneCandidate(Candidate candidate);
    int deleteCandidate(Map<String,Object> map);
    int updateCandidate(Candidate candidate);
    List<Candidate> queryByParams(Map<String,Object> map);
    List<String> queryIdByParams(Map<String,Object> map);
}
