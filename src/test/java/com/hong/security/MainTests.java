package com.hong.security;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wanghong
 * @date 2020/05/12 10:46
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTests {

    @Test
    public void test(){
        String authHeader = "Authorization: Bearer jsfd3j5kljlegjsd235356jkjsadgsdg";
        authHeader.replace("Authorization: Bearer ".trim(), StringUtils.EMPTY);
        System.out.println(authHeader);
        String tokenValue = StringUtils.trim(authHeader.replace("Authorization: Bearer ".trim(), StringUtils.EMPTY));
        System.out.println(tokenValue);
    }

}
