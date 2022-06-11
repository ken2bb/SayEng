package com.pjwxw.sayeng.controller;

import com.pjwxw.sayeng.feignHttps.WeChatAPI;
import com.pjwxw.sayeng.utils.GsonUtil;
import feign.Feign;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Map;

@RestController
public class VideoController {

    @RequestMapping("/getWxInfo")
    public Map<String,Object> testWeChat(String code){
        WeChatAPI weChatAPI = Feign.builder().target(WeChatAPI.class,"https://api.weixin.qq.com");
        String result = weChatAPI.getUserInfo("wx2586549f865eb1f6","d4c5774d87a51bbd18a5f0150d879cff",code);
        Map<String,Object> resultMap = GsonUtil.parseGson(result);
        return resultMap;
    }

    @RequestMapping("/playVideo")
    public void getVideo(HttpServletRequest request, HttpServletResponse response, String filePath){
        response.reset();
        String rangeStr = request.getHeader("Range");
        System.out.println(rangeStr);
        try {
            OutputStream outputStream = response.getOutputStream();
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath,"r");
            long size = randomAccessFile.length();
            long start = 0;
            if(!StringUtils.isEmpty(rangeStr)){
                start = Long.valueOf(rangeStr.substring(rangeStr.indexOf("=")+1,rangeStr.indexOf("-")));
            }

            response.setHeader("Content-Type","video/mov");
            response.setHeader("Content-Length",String.valueOf(size-start));
            response.setHeader("Content-Range","bytes "+start+"-"+(size-1)+"/"+size);
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            byte[] buffer = new byte[1024];
            randomAccessFile.seek(start);
            int read = -1;
            while ((read = randomAccessFile.read(buffer)) != -1) {
                outputStream.write(buffer,0,read);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
