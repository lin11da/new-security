package com.example.config.security.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * TODO
 *
 * @author chen
 * @version 1.0
 * @date 2021/11/25 10:46
 */
public class ResponseUtil {
    public static void send(HttpServletResponse response, Map<String, Object> data){

        JSONObject json = new JSONObject(data);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            out.append(json.toString());
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (out != null){
                out.close();
            }
        }
    }

}
