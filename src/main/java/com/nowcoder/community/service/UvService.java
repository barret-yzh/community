package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UvService {

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    private RedisTemplate redisTemplate;

    //将指定的IP计入UV
    public void recordUV(String ip) {
        String uvKey = RedisKeyUtil.getUVKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(uvKey, ip);
    }

    //统计指定日期范围内的UV(以ip为基准)
    public long calculateUV(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (start.after(end)) {
            throw new IllegalArgumentException("开始日期不能大于结束日期");
        }
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String key = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE, 1);
        }
        //合并数据
        String uvKey = RedisKeyUtil.getUVKey(df.format(start), df.format(end));
        redisTemplate.opsForHyperLogLog().union(uvKey, keyList.toArray());
        return redisTemplate.opsForHyperLogLog().size(uvKey);
    }

    //将指定用户记录到DAU
    public void recordDAU(int userId) {
        String dauKey = RedisKeyUtil.getDAUKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(dauKey, userId, true);
    }

    //统计日期范围内的活跃用户
    public long calculateDAU(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (start.after(end)) {
            throw new IllegalArgumentException("开始日期不能大于结束日期");
        }
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            String dauKey = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            keyList.add(dauKey.getBytes());
            calendar.add(Calendar.DATE, 1);
        }
        //进行OR运算
        return (long) redisTemplate.execute((RedisCallback) connection -> {
            String dauKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
            connection.bitOp(RedisStringCommands.BitOperation.OR,
                    dauKey.getBytes(), keyList.toArray(new byte[0][0]));
            return connection.bitCount(dauKey.getBytes());
        });
    }
}