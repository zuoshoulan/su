package com.su.chatgo.springbootstomp;

/**
 * @Author Weikang Lan
 * @Created 2019-03-06 16:03
 */

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 消息载体
 * Created by ejiyuan on 2018-7-11
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class ChatMessage {

    private String content;
    private String sender;
    private String receiver;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


}