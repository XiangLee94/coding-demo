package indi.xianglee94.springdemo.interfaced;

public class InterfaceDemo {
    public static void main(String[] args) {
        Object o = new Object();
        AllparentInterface trans = (Interface1 & Interface2) o;

    }
}

interface Interface1 extends AllparentInterface{

}

interface Interface2 extends AllparentInterface{

}

interface  AllparentInterface{

}

class Impl implements Interface1 , Interface2{

}
