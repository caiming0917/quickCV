package com.caijuan.springmybatis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCategory {
    // 通知开关 true : 开启通知 ； false : 关闭通知
    private boolean noticeSwitch;
    // 通知方式 {<通知方式：是否开启>} 例如：{"sms":true,"lanxin":fasle}
    private Map<String, Boolean> noticeMode;

    public boolean enableNotice() {
        return noticeSwitch;
    }

    public boolean supportMode(String way){
        return noticeMode.get(way);
    }
}