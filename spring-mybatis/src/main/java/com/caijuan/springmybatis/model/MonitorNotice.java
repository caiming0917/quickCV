package com.caijuan.springmybatis.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MonitorNotice {
    // 设备连接状态通知设置
    private NoticeCategory connectNotice;
    // 数据传输状态通知设置
    private NoticeCategory transNotice;
    // 设备巡检异常通知设置
    private NoticeCategory exceptionNotice;
}
