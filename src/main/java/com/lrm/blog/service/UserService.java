package com.lrm.blog.service;

import com.lrm.blog.po.User;

public interface UserService {

    User checkUser(String username, String password);

    void updateUser(User user);

    User getUser(Long id);

}
