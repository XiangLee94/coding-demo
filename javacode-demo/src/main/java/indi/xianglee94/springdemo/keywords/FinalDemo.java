package indi.xianglee94.springdemo.keywords;

public class FinalDemo {
    public static void main(String[] args) {
        testFinal1();
    }

    static void testFinal1() {
        String a = "helloworld";
        String aa = "hello" + "world";
        final String b = "hello";
        String c = "hello";

        String d = b + "world";
        String e = c + "world";

        System.out.println(a == aa);//false
        System.out.println(a == d);//false
        System.out.println(a == e);//false
    }
}

class BlankFinal {
    //blank final 必须在每个构造器里对blank final 进行赋值
    private final int i = 0;
    private final int j;
    private final String s;

    private BlankFinal(){
        this.j = 1;
        this.s = "";
    }

    private BlankFinal(int a){
        j = a * 1;
        s = "";
    }

}