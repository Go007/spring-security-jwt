package com.hong.security.util;

import com.hong.security.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

/**
 * 生成JWT token工具类
 */
public class TokenUtil {

    public static String createToken(String userName, String deviceId) {
        String token = String.format("%s.%s.%s.%s", String.valueOf(System.currentTimeMillis()), Constants.SECRET_KEY, userName, deviceId);
        token = DigestUtils.md5Hex(token);
        return token;
    }

    public static String createJWT(String userName, String oauths) {
        String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim(Constants.AUTHORITIES, oauths)
                // 用户名写入标题
                .setSubject(userName)
                // 有效期设置
                .setExpiration(new Date())  // DateUtil.getNextDate(new Date(), Constants.EXPIRE_DAY)
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, Constants.SECRET_KEY).compact();
        return JWT;
    }

    public static String createRefreshJWT(String userName, String oauths) {
        String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim(Constants.AUTHORITIES, oauths)
                // 用户名写入标题
                .setSubject(userName)
                // 有效期设置
                .setExpiration(new Date())  //DateUtil.getNextDate(new Date(), Constants.EXPIRE_DAY)
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, Constants.SECRET_KEY).compact();
        return JWT;
    }

    public static Claims validate(String token) {
        try {
            //如果token校验通过,或者缓存中有匹配的token值，返回true
            return parseJWT(token);
        } catch (Exception e) {
            throw e;
        }

    }

    public static Claims parseJWT(String token) {
        Claims claims = Jwts.parser()
                // 验签
                .setSigningKey(Constants.SECRET_KEY)
                // 去掉 Bearer
                .parseClaimsJws(token.replace(Constants.TOKEN_PREFIX, "")).getBody();
        return claims;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); // 获取开始时间
        System.out.println(createJWT("jhon", "USER"));
        long endTime = System.currentTimeMillis(); // 获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IkFETUlOIiwic3ViIjoiamhvbiIsImV4cCI6MTUwMDEwOTg3NX0.bo3lXTfVPMw-tqMficag_vweO28_UY5JDhPW1JL8JSPMLPekewrHZJtilVKTVPy3e4eTlpznT0wKDomF_Jq72A";
        Claims claims = parseJWT(jwt);

        System.out.println(validate(jwt));
        System.out.println(claims.get(Constants.AUTHORITIES));
        System.out.println(claims.getExpiration());
        // System.out.println("当前时间:"+new Date(System.currentTimeMillis()));
        // System.out.println("当前时间:"+DateUtil.getNextDate(new Date(), 26));
    }
}
