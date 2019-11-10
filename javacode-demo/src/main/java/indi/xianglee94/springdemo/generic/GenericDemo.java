package indi.xianglee94.javacode.generic;

import java.util.ArrayList;
import java.util.List;

public class GenericDemo {
    /*
    1. 概述

    泛型，即“参数化类型”。一提到参数，最熟悉的就是定义方法时有形参，然后调用此方法时传递实参。那么参数化类型怎么理解呢？

    顾名思义，就是将类型由原来的具体的类型参数化，类似于方法中的变量参数，此时类型也定义成参数形式（可以称之为类型形参），

    然后在使用/调用时传入具体的类型（类型实参）。

    泛型的本质是为了参数化类型（在不创建新的类型的情况下，通过泛型指定的不同类型来控制形参具体限制的类型）。也就是说在泛型使用过程中，

    操作的数据类型被指定为一个参数，这种参数类型可以用在类、接口和方法中，分别被称为泛型类、泛型接口、泛型方法。
    * */

    /*
    2. 一个栗子
    * */
    public static void demo() {
        List arrayList = new ArrayList();
        arrayList.add("aaaa");
        arrayList.add(100);
        //会抛出ClassCastException，因此需要声明为List<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < arrayList.size(); i++) {
            String item = (String) arrayList.get(i);
            System.out.println("泛型测试 item = " + item);
        }
    }

    /*
    3.泛型只在编译阶段有效。看下面的代码
    * */
    public static void testType() {
        List<String> stringArrayList = new ArrayList<String>();
        List<Integer> integerArrayList = new ArrayList<Integer>();

        Class classStringArrayList = stringArrayList.getClass();
        Class classIntegerArrayList = integerArrayList.getClass();

        if (classStringArrayList.equals(classIntegerArrayList)) {
            System.out.println("泛型测试 类型相同");
        }
    }


    /*
    4. 泛型的使用
    泛型有三种使用方式，分别为：泛型类、泛型接口、泛型方法
     */

    public static void testUserGenericClass() {
        //泛型的类型参数只能是类类型（包括自定义类），不能是简单类型
        //传入的实参类型需与泛型的类型参数类型相同，即为Integer. java8新增类型推断，new的<>可以不写
        Generic<Integer> genericInteger = new Generic<>(123456);

        //传入的实参类型需与泛型的类型参数类型相同，即为String.
        Generic<String> genericString = new Generic<>("key_vlaue");
        System.out.println("泛型测试 key is " + genericInteger.getKey());
        System.out.println("泛型测试 key is " + genericString.getKey());

        //定义的泛型类，就一定要传入泛型类型实参么？并不是这样，在使用泛型的时候如果传入泛型实参，则会根据传入的泛型实参做相应的限制，此时泛型才会起到本应起到的限制作用。
        // 如果不传入泛型类型实参的话，在泛型类中使用泛型的方法或成员变量定义的类型可以为任何的类型
        Generic generic = new Generic("111111");
        Generic generic1 = new Generic(4444);
        Generic generic2 = new Generic(55.55);
        Generic generic3 = new Generic(false);
        System.out.println("泛型测试 key is " + generic.getKey());
        System.out.println("泛型测试 key is " + generic1.getKey());
        System.out.println("泛型测试 key is " + generic2.getKey());
        System.out.println("泛型测试 key is " + generic3.getKey());

    }


}


//此处T可以随便写为任意标识，T常见的如T、E、K、V等形式的参数常用于表示泛型
//在实例化泛型类时，必须指定T的具体类型
class Generic<T extends Object> {
    //key这个成员变量的类型为T,T的类型由外部指定
    private T key;

    public Generic(T key) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
        this.key = key;
    }

    public T getKey() { //泛型方法getKey的返回值类型为T，T的类型由外部指定
        return key;
    }
    //    static方法声明为在泛型类里边时，静态方法无法访问类上定义的泛型，如下方法编译不能通过
    //    public static  void show(T t){
    //    }

    //    static 必须声明为泛型方法，自己有泛型
    public static <T> void show(T t) {

    }
}



