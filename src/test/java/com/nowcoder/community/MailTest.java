package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {

    @Autowired
    private MailClient client;
    @Autowired
    private TemplateEngine templateEngine;

    //测试发送普通邮件
    @Test
    public void testMail(){
        client.sendMail("barretyzh@sina.com","Test","hello world");
    }

    //测试发送html文件,这里想要根据用户名设置对应的html所以用了Thymeleaf
    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","叶子航");
        String html = templateEngine.process("mail/demo", context);
        client.sendMail("barretyzh@sina.com","TestHtml",html);
    }
}
