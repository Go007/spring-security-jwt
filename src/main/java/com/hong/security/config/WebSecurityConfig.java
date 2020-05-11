package com.hong.security.config;

import com.hong.security.filter.JwtAuthenticationTokenFilter;
import com.hong.security.handler.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author wanghong
 * @date 2020/05/11 21:57
 *  Spring Security 配置
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 配置 url拦截 没有token 让其去登录获取token
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // 由于使用的是JWT，我们这里不需要csrf
        http.csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                // 允许对于网站静态资源的无授权访问
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js", "/**/*.png","/info","/**/*.apk").permitAll()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/v1/user/**").permitAll()
                .antMatchers("/v1/personCenter/**").permitAll()
                .antMatchers("/v1/pay/notify").permitAll()
                .antMatchers("/v1/goods/callTraffics/**").permitAll()
                .antMatchers("/v1/qq/goods/**").permitAll()
                .antMatchers("/v1/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        //自定义无权限访问错误
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        // 添加JWT filter
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        http.headers().cacheControl();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
}
