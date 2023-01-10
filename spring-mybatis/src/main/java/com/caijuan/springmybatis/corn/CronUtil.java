package com.caijuan.springmybatis.corn;

import lombok.extern.slf4j.Slf4j;

/**
 * 构建日周月的Cron表达式
 */
@Slf4j
public class CronUtil {

    public static String createCronExp(CronEntity cronEntity) {
        StringBuffer cronExp = new StringBuffer("");
        if (cronEntity.getFrequency() == null) {
            cronEntity.setFrequency(1);
        }
        if (null != cronEntity.getMinute() && null != cronEntity.getHour()) {
            cronExp.append(0).append(" ");
            cronExp.append(cronEntity.getMinute()).append(" ");
            cronExp.append(cronEntity.getHour()).append(" ");
            if (cronEntity.getCycle().equals("DAY")) {
                cronExp.append("*/").append(cronEntity.getFrequency()).append(" ");
                cronExp.append("* ");
                cronExp.append("?");
            } else if (cronEntity.getCycle().equals("WEEK")) {
                cronExp.append("? ");
                cronExp.append("* ");
                String[] weeks = cronEntity.getDayOfWeeks();
                for (int i = 0; i < weeks.length; i++) {
                    if (i == 0) {
                        cronExp.append(weeks[i]);
                    } else {
                        cronExp.append(",").append(weeks[i]);
                    }
                }
            } else if (cronEntity.getCycle().equals("MONTH")) {
                String[] days = cronEntity.getDayOfMonths();
                for (int i = 0; i < days.length; i++) {
                    if (i == 0) {
                        cronExp.append(days[i]);
                    } else {
                        cronExp.append(",").append(days[i]);
                    }
                }
                cronExp.append(" * ");
                cronExp.append("?");
            }
        } else {
            cronExp.append("Missing time parameter");
            log.info("分钟小时参数未设置");
        }
        return cronExp.toString();
    }

    //测试
    public static void main(String[] args) {
        CronEntity cronEntity = new CronEntity();
        cronEntity.setFrequency(1);
        cronEntity.setCycle("WEEK");
        String[] str = {"2", "3"};
        cronEntity.setDayOfWeeks(str);
        cronEntity.setMinute(20);
        cronEntity.setHour(12);
        String cronExp = createCronExp(cronEntity);
        log.info(cronExp);
    }
}
