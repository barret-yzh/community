package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.dao.CommunityConstant;
import com.nowcoder.community.util.MailClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    // 消费点赞、评论、关注事件
    @KafkaListener(topics = {TOPIC_LIKE, TOPIC_COMMENT, TOPIC_FOLLOW, TOPIC_CANCEL_LIKE,TOPIC_CANCEL_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误");
            return;
        }
        //发送系统通知
        Message message = new Message();
        //id为 1 代表系统用户
        message.setFromId(SYSTEM_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    // 消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }
        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);
    }

    // 消费删帖事件
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }
        //级联删除评论和回复
        int postId = event.getEntityId();
        discussPostService.updateStatus(postId, 2);
        elasticsearchService.deleteDiscussPost(event.getEntityId());
        List<Comment> comments = commentService.findCommentsByEntity(ENTITY_TYPE_POST, postId, 0, Integer.MAX_VALUE);////懒得分页
        for (int i = 0; i < comments.size(); i++) {
            List<Comment> replys = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comments.get(i).getId(), 0, Integer.MAX_VALUE);
            for (int j = 0; j < replys.size(); j++) {
                commentService.deleteCommentById(comments.get(i).getId());
            }
            commentService.deleteCommentById(comments.get(i).getId());
        }
    }

    // 消费发送邮件事件
    @KafkaListener(topics = {TOPIC_SENDEMAIL})
    public void handleSendEmail(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }
        Map<String, Object> content = (Map<String, Object>) event.getData().get("context");
        String email = (String) content.get("email");
        String url = (String) content.get("url");
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("url", url);
        String result = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(email, "激活账号", result);
    }
}