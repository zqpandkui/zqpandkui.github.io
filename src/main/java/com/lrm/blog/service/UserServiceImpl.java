package com.lrm.blog.service;

import com.lrm.blog.dao.UserRepository;
import com.lrm.blog.po.User;
import com.lrm.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user.getUsername(),user.getNickname(),user.getPassword(),user.getEmail(),user.getAvatar());
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }
}
