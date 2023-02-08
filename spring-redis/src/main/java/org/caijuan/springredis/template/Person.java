package org.caijuan.springredis.template;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {

    private long uuid;

    private int age;

    public Person(long l, int i) {
        this.uuid = l;
        this.age = i;
    }
}
