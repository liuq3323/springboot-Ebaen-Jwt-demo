package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.service.DemoService;
import com.example.util.Result;
import com.example.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public Result demoTest(){
        Long id = 1L;
        String name = "Jack";
        String age = "11";
        List<User> list = demoService.hello();
        return Result.ok(list);
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.GET)
    public Result addUser(){
        String name = "liuq";
        String age = "27";
        User user = demoService.save(name,age);
        Map resultMap = new HashMap();
        resultMap.put("User",user);
        return Result.ok(resultMap);
    }

    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public Result updateUser(@RequestBody String posJson){
        JSONObject object = JSON.parseObject(posJson);
        Long id = object.getLong("id");
        String name = object.getString("name");
        String age = object.getString("age");
        User user = User.find.byId(id);
        user.setName(name);
        user.setAge(age);
        demoService.update(user);
        Map resultMap = new HashMap();
        resultMap.put("User",user);
        return Result.ok(resultMap);
    }

    @RequestMapping(value = "/delUser",method = RequestMethod.GET)
    public Result delUser(@RequestParam Long id){
        User user = User.find.byId(id);
        demoService.del(user);
        return Result.ok();
    }

//    @RequestMapping(value = "/error",method = RequestMethod.POST)
//    public Result error(){
//        return Result.fail("error");
//    }
}
