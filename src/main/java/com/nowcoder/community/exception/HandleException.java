package com.nowcoder.community.exception;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * 统一处理异常
 */

@ControllerAdvice(annotations = Controller.class)
public class HandleException {

    private static final Logger logger = LoggerFactory.getLogger(HandleException.class);

    @ExceptionHandler({Exception.class})
    //参数可以传很多
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常 " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }
        //判断是HTML请求还是AJAX请求
        String xRequestWith = request.getHeader("x-requested-with");
        //AJAX请求
        if ("XMLHttpRequest".equals(xRequestWith)) {
            //response.setContentType("application/json"); //浏览器会自动转成json对象
            //浏览器返回普通字符串
            response.setContentType("application/plain;charSet=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常"));
        } else {
            //HTML请求
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}