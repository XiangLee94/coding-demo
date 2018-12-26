package indi.xianglee94.javacode.langdemo;

public class StringDemo {
    public static void main(String[] args) {
        testIntern();
    }

    static void testIntern(){
        String a = "helloworld";
        final String b = "hello";
        String c = "hello";

        String d = b+"world";
        String e = c+"world";

//        e = e.intern(); //注意intern和不加的区别

        System.out.println(a==d);
        System.out.println(a==e);
    }
}
