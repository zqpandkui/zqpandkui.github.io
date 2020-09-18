package com.lrm.blog.dao;

import com.lrm.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username,String password);

    @Transactional
    @Modifying
    @Query("update User u set u.username = ?1,u.nickname= ?2,u.password= ?3,u.email= ?4,u.avatar= ?5 where u.id = 1")
    void updateUser(String username,String nickname,String password,String email,String avatar);
}
