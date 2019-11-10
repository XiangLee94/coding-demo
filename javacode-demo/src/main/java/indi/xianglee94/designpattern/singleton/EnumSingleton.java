package indi.xianglee94.designpattern.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum EnumSingleton {
    INSTANACE;
    public EnumSingleton getInstanace(){
        return INSTANACE;
    }

    public int getValue(){
        return 1;
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println(EnumSingleton.INSTANACE.getValue());
        reflectCrack();
    }

    public static void reflectCrack() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {

        Class<EnumSingleton> singletonClass = (Class<EnumSingleton>) Class.forName("indi.xianglee94.designpattern.singleton.EnumSingleton");
        Constructor<EnumSingleton> c = singletonClass.getDeclaredConstructor(null);
        c.setAccessible(true);
        EnumSingleton singleton1 = c.newInstance();
        System.out.println(singleton1.getValue());
    }


}
