package indi.xianglee94.javacode.bit;

public class BitDemo {
    public static void main(String[] args) {
        int a = 032;
        int b = 26;
        int c = 0x1A;
        System.out.println(a==b);
        System.out.println(c==b);
    }

    /**
     * @Description
     * 去掉二进制数的最后一个1
     **/
    public static int removeLastOne(int a) {
        return a & (a - 1);
    }

    /**
     * @Description
     * 只保留二进制数的最后一个1
     * 计算机中的符号数有三种表示方法，即原码、反码和补码。三种表示方法均有符号位和数值位两部分，符号位都是用0表示“正”，用1表示“负”，而数值位，三种表示方法各不相同。
     * 在计算机系统中，数值一律用补码来表示和存储。原因在于，使用补码，可以将符号位和数值域统一处理；同时，加法和减法也可以统一处理。此外，补码与原码相互转换，其运算过程是相同的，不需要额外的硬件电路。
     * 正数的原码补码相同，负数的补码是对应正数反码加1
     * for example 9 原码 1001 补码 1001
     *            -9 补码 0111
     **/
    public static int onlyStayLastOne(int a){
        return a & -a;
    }

    public static void leftMove(){

    }

    public static void testSystemOfNumeration(){
        /*
        * 十进制，八进制，十六进制的表示
        * 八进制前边加0，十六进制加0x，java不能直接表示二进制
        * */
        int a = 032;
        int b = 26;
        int c = 0x1A;
        System.out.println(a==b); //true
        System.out.println(c==b); //true
    }

}
