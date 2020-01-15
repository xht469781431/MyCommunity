package com.xht.community.controller;

import com.xht.community.dto.AccessTokenDTO;
import com.xht.community.dto.GithubUser;
import com.xht.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    //授权的回调路径
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state){

        //创建一个Access对象作为参数传递
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO(
                "6273d47212d9111ed940",
                "3f139c7b4186462f4fa78deef6194600da6b7f0f",
                code,
                "http://localhost:8887/callback",
                state);

        //调用方法得到accessToken字符串 用来获取User信息
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);

        //使用accessToken获取User信息
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());


        return "index";
    }
}
