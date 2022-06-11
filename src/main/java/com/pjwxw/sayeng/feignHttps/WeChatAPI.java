package com.pjwxw.sayeng.feignHttps;

import feign.Param;
import feign.RequestLine;

public interface WeChatAPI {

    //
    @RequestLine("GET /sns/jscode2session?appid={appid}&secret={secret}&js_code={code}&grant_type=authorization_code")
    String getUserInfo(@Param("appid")String appid,@Param("secret")String secret,@Param("code")String code);

}
