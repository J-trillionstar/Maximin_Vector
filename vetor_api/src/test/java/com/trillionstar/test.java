package com.trillionstar;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class test {
    @Test
    public void test1(){
        String str = "{\"name\":\"trillionstar\",\"sex\":\"男\"}";
        Map<String,String> parse = (Map)JSON.parse(str);
        System.out.println(parse.get("sex"));
    }
}
