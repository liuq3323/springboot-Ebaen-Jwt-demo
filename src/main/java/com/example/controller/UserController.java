package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.service.MyUserRepositoryService;
import com.example.util.Result;
import com.example.vo.MyUser;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController{

    @Autowired
    private MyUserRepositoryService myUserRepositoryService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @RequestMapping(value = "/signup" , method = RequestMethod.POST)
    public Result signUp(@RequestBody String posJson) throws Exception{
        MyUser user = new MyUser();
        JsonNode node = toJsonNode(posJson);
        if(!validParam(node,"username"))
            return Result.fail("","username can not be null");
        if(!validParam(node,"username"))
            return Result.fail("","password can not be null");
        user.setUsername(node.get("username").asText());
        user.setPassword(bCryptPasswordEncoder.encode(node.get("password").asText()));
        myUserRepositoryService.save(user);
        return Result.ok("success signup");
    }


}
