package com.example.vo;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "User")
public class User extends Model{
    @Id
    @Column(name = "id")
    public Long id ;

    @Column(name = "name")
    public String name ;

    @Column(name = "age")
    public String age;

    @Column(name = "delFlag")
    public Integer delFlag;


    public static Finder<Long, User> find = new Finder<Long, User>(User.class);

    public static User findById(Long id){
        User user = User.find.byId(id);
        return user;
    }

    public User(Long id, String name, String age) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(String name, String age) {
        super();
        this.name = name;
        this.age = age;
    }

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
