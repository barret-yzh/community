package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private int id;//唯一标识
    private int fromId;//发送者id
    private int toId;//接收者id
    private String conversationId;//会话id
    private String content;//内容
    private int status;//0-未读;1-已读;2-删除
    private Date createTime;//创建时间
}