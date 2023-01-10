package com.caijuan.springmybatis.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

// 1.定时调度器
@Component
public class Schedule {
    Logger log = LoggerFactory.getLogger(Schedule.class);

    //cron表达式：每隔5秒执行一次
    @Scheduled(cron = "0/5 * * * * *")
    public void scheduled(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh点mm分ss秒");
        // 将date日期解析为字符串
        String date = simpleDateFormat.format(new Date());
        log.info("当前时间：" + date);
    }

    /**
     * 每天执行一次，每天晚上12点
     */
    @Scheduled(cron = "0 0 0 */1 * * ")
    public void timing() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh点mm分ss秒");
                String date = simpleDateFormat.format(new Date());
                log.info("当前时间 2：" + date);
            }
        }, 0, 1000 * 60 * 60 * 24L);//0表示无延迟，一天触发一次
    }
}
