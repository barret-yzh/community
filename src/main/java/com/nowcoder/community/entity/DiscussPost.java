package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "discusspost")
public class DiscussPost {

    @Id
    private int id;//唯一标识

    @Field(type = FieldType.Integer)
    private int userId;//用户id

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;//帖子标题

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;//帖子内容

    @Field(type = FieldType.Integer)
    private int type;//0-普通; 1-置顶

    @Field(type = FieldType.Integer)
    private int status;//0-正常; 1-精华; 2-拉黑

    @Field(type = FieldType.Date)
    private Date createTime;//创建时间

    @Field(type = FieldType.Integer)
    private int commentCount;//评论数量

    @Field(type = FieldType.Double)
    private double score;//帖子分数
}