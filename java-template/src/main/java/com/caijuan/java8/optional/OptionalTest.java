package com.caijuan.java8.optional;

import java.util.Optional;

/**
 * https://juejin.cn/post/7081243756044746783
 */
public class OptionalTest {
    public static void main(String[] args) {
        test03();
    }

    private static void test03(){
        String value = "2";
        String orElse = Optional.ofNullable(value).orElse("1");
        System.out.println(orElse);     //2

        orElse = Optional.ofNullable(value).orElseGet(OptionalTest::get);
        System.out.println(orElse);  // 2

        value = null;
        orElse = Optional.ofNullable(value).orElse("1");
        System.out.println(orElse);     //1

        orElse = Optional.ofNullable(value).orElseGet(OptionalTest::get);
        System.out.println(orElse);  // 123

        orElse = Optional.ofNullable(value).orElseThrow(() -> new RuntimeException("不存在值"));
        System.out.println(orElse);
    }

    private static void test02() {
        User user = new User();
        user.setUsername("我是小四哥");
        Optional<User> optional = Optional.ofNullable(user);
        System.out.println(optional);
        System.out.println(optional.isPresent());
        optional.ifPresent(s -> System.out.println(s));
        Optional<User> optional1 = optional.filter(v -> v.getUsername().equals("我是小四哥"));
        System.out.println(optional1);
        System.out.println(optional.get());

        Optional<String> optional2 = Optional.ofNullable(user).map(OptionalTest::getMap);
        System.out.println(optional2);

        optional2 = Optional.ofNullable(user).flatMap(OptionalTest::getFlatMap);
        System.out.println(optional2);

        System.out.println("==============");
        user = null;
        optional = Optional.ofNullable(user);
        System.out.println(optional);
        System.out.println(optional.isPresent());
        // 不处理
        optional.ifPresent(s -> System.out.println(s));
        optional1 = optional.filter(v -> v.getUsername().equals("我是小四哥"));
        System.out.println(optional1);

        // Optional.empty
        optional2 = Optional.ofNullable(user).map(OptionalTest::getMap);
        System.out.println(optional2);

        optional2 = Optional.ofNullable(user).flatMap(OptionalTest::getFlatMap);
        System.out.println(optional2);
        // 异常
//        System.out.println(optional.get());
    }

    private static void test01() {
        User user = null;
        Optional<User> optional = Optional.of(user);
        System.out.println(user);
    }

    public static String getMap(User user){
        return user.getUsername();
    }

    public static Optional<String> getFlatMap(User user){
        return Optional.ofNullable(user).map(User::getUsername);
    }

    public static String get(){
        return "123";
    }
}