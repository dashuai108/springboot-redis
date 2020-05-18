package com.example.clund.myredis.service;

import com.example.clund.myredis.entity.User;
import com.example.clund.myredis.repository.UserRepository;
import com.example.clund.myredis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserRepository userRepository;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User findUserByName(String name) {

        User user = null;
        if (redisUtil.hasKey(name)) {
            user = (User) redisUtil.get(name);

        } else {
            user = userRepository.getUserByName(name);
            redisUtil.set(name, user);
        }

        return user;
    }


    public User findById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
