package com.trillionstar.controller;

import com.trillionstar.utils.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/util")
public class UtilController {
    @GetMapping("getuuid")
    public String getUUid(){
        return UserUtil.get12UUID();
    }
}
