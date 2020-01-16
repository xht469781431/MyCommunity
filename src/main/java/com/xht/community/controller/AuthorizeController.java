package com.xht.community.controller;

import com.xht.community.dto.AccessTokenDTO;
import com.xht.community.dto.GithubUser;
import com.xht.community.mapper.UserMapper;
import com.xht.community.model.User;
import com.xht.community.provider.GithubProvider;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;
import java.util.UUID;

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

    @Autowired(required = false)
    private UserMapper userMapper;

    //授权的回调路径
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response){

        //创建一个Access对象作为参数传递
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO(clientId,clientSecret,code,redirectUri,state);

        //调用方法得到accessToken字符串 用来获取User信息
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);

        //使用accessToken获取User信息
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if(githubUser!=null){
            User user = new User();
            user.setAccountId(githubUser.getId());
            user.setName(githubUser.getName());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setCreateTime(System.currentTimeMillis());
            user.setModifiedTime(user.getCreateTime());
            Integer integer = userMapper.insertUser(user);

            //token写入到cookie中
            response.addCookie(new Cookie("token",token));
        }else{

        }

        return "redirect:/";
    }
}
