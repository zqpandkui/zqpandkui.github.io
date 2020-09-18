package com.lrm.blog.web;


import com.lrm.blog.po.Message;
import com.lrm.blog.po.User;
import com.lrm.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;


    @Value("${message.avatar}")
    private String avatar;


    @GetMapping("/message")
    public String message(Model model){
        model.addAttribute("messages",messageService.listMessage());
        return "message";
    }

    @GetMapping("/messageList")
    public String messageList(Model model){
        model.addAttribute("messages",messageService.listMessage());
        return "message :: commentList";
    }

    @PostMapping("/message")
    public String post(Message message, HttpSession session){
        User user = (User)session.getAttribute("user");
        if (user != null){
            message.setAvatar(user.getAvatar());
            System.out.println(message.getAvatar());
            message.setAdminMessage(true);
        }else{
            message.setAvatar(avatar);
            message.setAdminMessage(false);
        }
        messageService.saveMessage(message);
        return "redirect:/messageList";
    }

}
