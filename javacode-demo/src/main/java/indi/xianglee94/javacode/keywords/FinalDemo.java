package indi.xianglee94.javacode.keywords;

public class FinalDemo {
    public static void main(String[] args) {
        testFinal1();
    }

    static void testFinal1(){
        String a = "helloworld";
        final String b = "hello";
        String c = "hello";

        String d = b+"world";
        String e = c+"world";

        System.out.println(a==d);
        System.out.println(a==e);
    }
}
