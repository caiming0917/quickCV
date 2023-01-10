package com.caijuan.springmybatis.corn;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class CronEntity extends Model<CronEntity> {
    /**
     * 周期: DAY：每天，WEEK：每周，MONTH：每月
     */
    String cycle;

    /** 一周的哪几天 */
    String[] dayOfWeeks;

    /** 一个月的哪几天 */
    String[] dayOfMonths;

    /** 频次，一天执行多少次 */
    Integer frequency;

    /** 时 */
    Integer hour;

    /** 分 */
    Integer minute;
}
