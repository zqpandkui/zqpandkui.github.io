package com.lrm.blog.service;

import com.lrm.blog.po.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface MessageService {

    List<Message> listMessage();

    Message saveMessage(Message message);
}
