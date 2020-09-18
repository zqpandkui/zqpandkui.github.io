package com.lrm.blog.web.admin;

import com.lrm.blog.po.User;
import com.lrm.blog.service.UserService;
import com.lrm.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;


    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
         User user = userService.checkUser(username,password);
         if(user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
         }else{
             attributes.addFlashAttribute("message","用户名或密码错误");
             return "redirect:/admin";
         }
    }

    @GetMapping("/logout")
    public String logput(HttpSession session){
         session.removeAttribute("user");
         return "redirect:/admin";
    }

    @GetMapping("/persional/{id}")
    public String persional(@PathVariable Long id,
                            HttpSession session,
                            RedirectAttributes attributes){
        User user = userService.getUser(id);
         session.setAttribute("user",user);
         return "admin/persional";
    }

    @PostMapping("/persional/{id}")
    public String editpersional(@PathVariable Long id,
                                @RequestParam String username,
                                @RequestParam String nickname,
                                @RequestParam String password,
                                @RequestParam String email,
                                @RequestParam String avatar,
                                HttpSession session,
                                RedirectAttributes attributes){
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(MD5Utils.code(password));
        user.setEmail(email);
        user.setAvatar(avatar);
        userService.updateUser(user);
        attributes.addFlashAttribute("message","修改成功");
        return "redirect:/admin/persional/"+id;
    }
}
