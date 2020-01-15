package com.xht.community.provider;

import com.alibaba.fastjson.JSON;
import com.xht.community.dto.AccessTokenDTO;
import com.xht.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

//把这个类给Spring容器托管
@Component
public class GithubProvider {

     public String getAccessToken(AccessTokenDTO accessTokenDTO){
        //mediaType 规定数据格式 和 字符集
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        //创建一个OkHttpClient
        OkHttpClient client = new OkHttpClient();

        //创建一个RequestBody用来发送post请求
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        //传参,发送请求
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        //获取返回信息
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println("string:"+string);
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public GithubUser getUser(String accessTokenDTO){
        //创建一个OKhttpClient
        OkHttpClient client = new OkHttpClient();

        //创建一个post请求 获取User信息
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessTokenDTO)
                .build();

        //接收返回信息
        try (Response response = client.newCall(request).execute()) {
            //用String接收,然后使用fastjson转为对象
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }

        return null;
    }

}
