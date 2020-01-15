package com.xht.community.controller;

import com.xht.community.dto.AccessTokenDTO;
import com.xht.community.dto.GithubUser;
import com.xht.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirectUri}")
    private String redirectUri;

    //授权的回调路径
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){

        //创建一个Access对象作为参数传递
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO(clientId,clientSecret,code,redirectUri,state);

        //调用方法得到accessToken字符串 用来获取User信息
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);

        //使用accessToken获取User信息
        GithubUser user = githubProvider.getUser(accessToken);

        if(user!=null){
            request.getSession().setAttribute("user",user);

        }else{

        }

        return "redirect:/";
    }
}
