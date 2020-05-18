package com.example.clund.myredis.controller;

import com.example.clund.myredis.entity.User;
import com.example.clund.myredis.lock.annotation.CacheLock;
import com.example.clund.myredis.lock.annotation.CacheParam;
import com.example.clund.myredis.service.UserService;
import com.example.clund.myredis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping(value = "/redis")
@SuppressWarnings("all")
public class RedisController {
    private static int ExpireTime = 600;   // redis中存储的过期时间60s

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/addUser")
    public void addUser() {
        Random r = new Random();
        int age = r.nextInt(100);
        long l = r.nextLong();

        User user = new User();
        user.setId(l);
        user.setAge(age + "");
        user.setCreateTime(new Date());
        user.setGuid("guid" + l);
        user.setName("name " + age);
        System.out.println(user.toString());
        userService.addUser(user);
    }

    @RequestMapping("/getname")
    public User findUserByName(String name) {
        return userService.findUserByName(name);
    }


    @RequestMapping("/set")
    public boolean redisset(String key, String value) {
        User userEntity = new User();
        userEntity.setId(Long.valueOf(1));
        userEntity.setGuid(String.valueOf(1));
        userEntity.setName("zhangsan");
        userEntity.setAge(String.valueOf(20));
        userEntity.setCreateTime(new Date());

        return redisUtil.set(key, userEntity, ExpireTime);

//        return redisUtil.set(key,value);
    }

    @RequestMapping("/get")
    public Object redisget(String key) {
        return redisUtil.get(key);
    }

    @RequestMapping("/expire")
    public boolean expire(String key) {
        return redisUtil.expire(key, ExpireTime);
    }

    @RequestMapping("/findById")
    public User findById(Long id) {
        return userService.findById(id);
    }

    @RequestMapping("/save")
    public void save(@ModelAttribute User user) {
        userService.save(user);
    }

    @RequestMapping("/deleteById")
    public void deleteById(Long id) {
        userService.deleteById(id);
    }


    @CacheLock(prefix = "ttt", expire = 10)
    @RequestMapping("/token")
    public String query(@CacheParam(name = "token") @RequestParam String token) {
        return "success -" + token;
    }
}
