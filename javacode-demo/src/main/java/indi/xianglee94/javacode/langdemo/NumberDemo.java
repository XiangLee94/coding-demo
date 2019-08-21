package indi.xianglee94.javacode.langdemo;

public class NumberDemo {
    public static void main(String[] args) {
        testInteger();
    }

    //Double 类型没有cache（常量池）
    public static void testDouble() {
        Double a = new Double(3d);
        Double b = new Double(3d);

        Double c = 3d;
        Double d = 3d;

        System.out.println(a == b);
        System.out.println(a.equals(b));

        System.out.println(c == d);
        System.out.println(c.equals(d));
    }


    // Integer 类型有cache(常量池) -128 and 127
    public static void testInteger() {
        Integer a = new Integer(3);
        Integer b = new Integer(3);

        Integer c = 3;
        Integer d = 3;

        Integer e = 129;
        Integer f = 129;


        System.out.println(a == b);
        System.out.println(a.equals(b));

        System.out.println(c == d);
        System.out.println(c.equals(d));

        System.out.println(e == f);
        System.out.println(e.equals(f));
    }

    public static void testLong() {
        Long a = new Long(3);
        Long b = new Long(3);

        Long c = 3l;
        Long d = 3l;

        System.out.println(a == b);
        System.out.println(a.equals(b));

        System.out.println(c == d);
        System.out.println(c.equals(d));
    }
}
