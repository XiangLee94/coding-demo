package indi.xianglee94.javacode.langdemo;

import java.lang.reflect.Array;

public class ClassDemo {
    static  String[] arr = new String[10];

    public static void main(String[] args) {
        Class<? extends String[]> c = arr.getClass();
        System.out.println(c.isArray());
    }
}
