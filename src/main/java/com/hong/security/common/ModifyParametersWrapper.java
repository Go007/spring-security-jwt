package com.hong.security.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * @author wanghong
 * @date 2020/05/19 10:50
 * 继承HttpServletRequestWrapper，创建装饰类，以达到修改HttpServletRequest参数的目的
 **/
public class ModifyParametersWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> parameterMap; // 所有参数的Map集合

    public ModifyParametersWrapper(HttpServletRequest request) {
        super(request);
        parameterMap = request.getParameterMap();
    }

    // 重写几个HttpServletRequestWrapper中的方法

    /**
     * 获取所有参数名
     *
     * @return 返回所有参数名
     */
    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> vector = new Vector<>(parameterMap.keySet());
        return vector.elements();
    }

    /**
     * 获取指定参数名的值，如果有重复的参数名，则返回第一个的值 接收一般变量 ，如text类型
     *
     * @param name 指定参数名
     * @return 指定参数名的值
     */
    @Override
    public String getParameter(String name) {
        String[] results = parameterMap.get(name);
        if (results == null || results.length <= 0)
            return null;
        else {
            System.out.println("before modify： " + results[0]);
            return modify(results[0]);
        }
    }

    /**
     * 获取指定参数名的所有值的数组，如：checkbox的所有数据
     * 接收数组变量 ，如checkbox类型
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] results = parameterMap.get(name);
        if (results == null || results.length <= 0)
            return null;
        else {
            int length = results.length;
            for (int i = 0; i < length; i++) {
                System.out.println("before modify2： " + results[i]);
                results[i] = modify(results[i]);
            }
            return results;
        }
    }

    /**
     * 自定义的一个简单修改原参数的方法，即：给原来的参数值前面添加了一个修改标志的字符串
     *
     * @param string 原参数值
     * @return 修改之后的值
     */
    private String modify(String string) {
        return "Modified: " + string;
    }
}

