package indi.xianglee94.javacode.java8;

/*
* 可变参数和数组
* */
public class TestVarArgus {
    public void test(int... a){

    }

//    下边函数重载编译不过
//    public void test(int[] a){
//
//    }

    public static void main(String[] args) {
        TestVarArgus testVarArgus = new TestVarArgus();
        int[] arr = new int[10];
        //可变参数兼容数组，数组不兼容可变参数
        testVarArgus.test(arr);
    }

}
