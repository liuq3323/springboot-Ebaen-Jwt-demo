package com.example.service;

import com.example.vo.User;
import io.ebean.EbeanServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {

    @Autowired
    EbeanServer server;

    public List<User> hello() {

        List<User> list = server.find(User.class).where().eq("name","Jack").findList();
        return list;
    }

    public User save(String name ,String age){
        User user = new User();
        user.age= age;
        user.name = name;
        user.delFlag = 0;
        server.save(user);
        return user;
    }

    public User update(User user){
        server.update(user);
        return user;
    }

    public void del(User user){
        user.setDelFlag(1);
        server.update(user);
    }
}
