package com.example.service;

import com.example.vo.MyUser;
import io.ebean.EbeanServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MyUserRepositoryService {

    @Autowired
    EbeanServer server;

    public List<MyUser> findByUsername(String username){

        List<MyUser> myUser = server.find(MyUser.class).where().eq("username" , username).findList();
        return myUser;

    }

    public void save(MyUser user){
        server.save(user);
    }
}
