package com.hong.security.controller;

import com.hong.security.common.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
