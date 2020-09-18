package com.lrm.blog.service;


import com.lrm.blog.dao.MessageRepository;
import com.lrm.blog.po.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> listMessage() {
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        List<Message> messages = messageRepository.findByParentMessageNull(sort);
        return eachMessage(messages);
    }

    @Transactional
    @Override
    public Message saveMessage(Message message) {
        Long parentMessageId = message.getParentMessage().getId();
        if (parentMessageId != -1){
            message.setParentMessage(messageRepository.findById(parentMessageId).get());
        } else{
            message.setParentMessage(null);
        }
        message.setCreateTime(new Date());
        return messageRepository.save(message);
    }

    //循环每个顶级的评论节点
    private List<Message> eachMessage(List<Message> messages){
        List<Message> messagesView = new ArrayList<>();
        for (Message message : messages){
            Message m = new Message();
            BeanUtils.copyProperties(message,m);
            messagesView.add(m);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(messagesView);
        return messagesView;
    }

    //root根节
    private void combineChildren(List<Message> messages){
        for (Message message : messages){
            List<Message> replys1 = message.getReplyMessages();
            for (Message reply1 : replys1){
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            message.setReplyMessages(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代集合
    private List<Message> tempReplys = new ArrayList<>();

    //递归迭代； message为被迭代对象；
    private void recursively(Message message){
        boolean bool = tempReplys.contains(message);
        if(!bool) tempReplys.add(message);  //顶节点添加到临时存放集合
        if (message.getReplyMessages().size() > 0){
            List<Message> replys = message.getReplyMessages();
            for (Message reply : replys){
                tempReplys.add(reply);
                if(reply.getReplyMessages().size()>0){
                    recursively(reply);
                }
            }
        }
    }
}
