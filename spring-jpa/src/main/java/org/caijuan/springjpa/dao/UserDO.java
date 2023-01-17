package org.caijuan.springjpa.dao;

import lombok.Data;
import lombok.experimental.Accessors;
//import org.caijuan.springjpa.kit.LongListType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.array.LongArrayType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "users")
@TypeDef(name = "long-array", typeClass = LongArrayType.class)
public class UserDO {

    /**
     * 用户编号
     */
//    @GeneratedValue(strategy = GenerationType.IDENTITY,  // strategy 设置使用数据库主键自增策略；
//            generator = "JDBC") // generator 设置插入完成后，查询最后生成的 ID 填充到该属性中。
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 账号
     */
    @Column(nullable = false)
    private String username;
    /**
     * 密码（明文）
     * <p>
     * ps：生产环境下，千万不要明文噢
     */
    @Column(nullable = false)
    private String password;

    /**
     * @OneToMany
     * https://www.cnblogs.com/h-huakai/p/16574835.html
     */
    @Column(name = "classify_ids",columnDefinition = "long[]")
    @Type(type = "long-array")
    private List<Long> ids;
    /**
     * 创建时间
     */
//    @Column(name = "create_time", nullable = false)
    private Date createTime;
}