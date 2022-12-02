package com.caijuan.java8;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Java8Tester {


    public static void printLine(){
        System.out.println("\n--------------------------------------\n");
    }

    public static class StringJoinerDome{
        /**
         * https://www.runoob.com/java/java8-optional-class.html
         */
        public static void main(String[] args) {
            StringJoiner sj1 = new StringJoiner(",");
            System.out.println("sj1('') => " + sj1);
            System.out.println(sj1.add("a").add("b").add("c"));

            StringJoiner sj2 = new StringJoiner(",", "[", "]");
            System.out.println("sj2 => " + sj2);
            System.out.println(sj2.add("a").add("b").add("c"));

            StringJoiner tmp = sj1.merge(sj2);
            System.out.println("tmp => " + tmp + ", sj1 => " + sj1 + ", sj2 => " + sj2);

            tmp = sj2.merge(sj1);
            System.out.println("tmp => " + tmp + ", sj2 => " + sj2 + ", sj1 => " + sj1);

            System.out.println(sj1.length());

            printLine();

            // 第一个参数是分隔符，第二个参数是list
            System.out.println(String.join(",", Arrays.asList("a", "b", "c")));
            // 第二个参数是可变数组
            System.out.println(String.join(",", "a", "b", "c"));

            printLine();

            List<String> list1 = Arrays.asList("a", "b", "c");
            System.out.println(list1.stream().collect(Collectors.joining(",")));

            List<String> list2 = Arrays.asList("a", "b", "c");
            System.out.println(list2.stream().collect(Collectors.joining(",", "[", "]")));

        }
    }

    /**
     * https://www.runoob.com/java/java8-optional-class.html
     */
    public static class OptionalDome{
        public static void main(String args[]){
            Java8Tester java8Tester = new Java8Tester();
            Integer value1 = null;
            Integer value2 = new Integer(10);

            // Optional.ofNullable - 允许传递为 null 参数
            Optional<Integer> a = Optional.ofNullable(value1);

            // Optional.of - 如果传递的参数是 null，抛出异常 NullPointerException
            Optional<Integer> b = Optional.of(value2);
            System.out.println(sum(a,b));
        }

        public static Integer sum(Optional<Integer> a, Optional<Integer> b){

            // Optional.isPresent - 判断值是否存在

            System.out.println("第一个参数值存在: " + a.isPresent());
            System.out.println("第二个参数值存在: " + b.isPresent());

            // Optional.orElse - 如果值存在，返回它，否则返回默认值
            Integer value1 = a.orElse(new Integer(0));

            //Optional.get - 获取值，值需要存在
            Integer value2 = b.get();
            return value1 + value2;
        }
    }


}
