package com.hong.security.controller;

import com.hong.security.common.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author wanghong
 * @date 2020/05/12 17:27
 * 个人账号
 **/
@RestController
@RequestMapping("/account")
@PreAuthorize("hasAuthority('USER')")
public class AccountController {

    @PostMapping("/query")
    public Result query(@RequestBody Map<String, Object> paramMap) {
        return new Result();
    }

    @PostMapping("/modify")
    public void modify(@RequestParam("name") String name) {
        System.out.println("到达Controller层，after modify:" + name);
    }

}
