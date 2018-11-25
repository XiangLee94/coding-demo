package com.lee.interfacedemo;

public class InterfaceDemo {
    public static void main(String[] args) {
        Object o = new Object();
        Interface1 interface1 = (Interface1 & Interface2) o;

    }
}

interface Interface1{

}

interface Interface2{

}

class Impl implements Interface1 , Interface2{

}
