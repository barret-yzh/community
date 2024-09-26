package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTicket {

    private int id;//唯一标识
    private int userId;//用户id
    private String ticket;//凭证
    private int status;//0-有效; 1-无效
    private Date expired;//过期日期
}
