package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
 
    private int id;//唯一标识
    private int userId;//用户id
    private int entityType;//被评论的实体的类型
    private int entityId;//被评论的目标的id
    private int targetId;//对评论的评论：有指向性
    private String content;//内容
    private int status;//0-正常,1-禁用
    private Date createTime;//创建时间
}