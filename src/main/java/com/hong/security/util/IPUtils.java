package com.hong.security.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * IP 工具类
 *
 */
public class IPUtils {
	
	/**
	 * 字符串转换成整形
	 */
	public static long ip2long(String ipAddress) {
		long result = 0;
		String[] ipAddressInArray = ipAddress.split("\\.");
		
		for (int i = 3; i >= 0; i--) {
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
			result |= ip << (i * 8);
		}
		
		return result;
	}
	
	/**
	 * 整形转换成字符串
	 */
	public static String long2ip(long ip) {
		return ((ip >> 24) & 0xFF) + "."
				+ ((ip >> 16) & 0xFF) + "."
				+ ((ip >> 8) & 0xFF) + "."
				+ (ip & 0xFF);
	}
	
	/**
	 * 获取客户端IP地址
	 * @param request
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request) {
		
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            
            if(index != -1) {
                return ip.substring(0,index);
            }
            else {
                return ip;
            }
        }
        
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        
        return request.getRemoteAddr();
    }
}
