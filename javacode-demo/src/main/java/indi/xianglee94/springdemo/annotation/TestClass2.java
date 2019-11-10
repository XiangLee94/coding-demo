package indi.xianglee94.javacode.annotation;

@MyAnnotation("Test2")
public class TestClass2 {
    public static void main(String[] args) {
        String a = "aaaaaaaaa";
        String b = new String(a);
        System.out.println(b);
        a="cccccccccccc";
        System.out.println(b);
    }
}
