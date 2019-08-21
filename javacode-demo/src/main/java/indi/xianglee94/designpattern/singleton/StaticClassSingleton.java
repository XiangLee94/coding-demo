package indi.xianglee94.designpattern.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class StaticClassSingleton {
    private StaticClassSingleton() {
    }

    public int getValue() {
        return 1;
    }

    private static class StaticClassSingletonHolder {
        static {
            System.out.println("静态加载");
        }

        private static final StaticClassSingleton instance = new StaticClassSingleton();
    }

    public static StaticClassSingleton getInstance() {
        return StaticClassSingletonHolder.instance;
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        reflectCrack();
    }

    public static void reflectCrack() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        StaticClassSingleton singleton = getInstance();
//        Class<StaticClassSingleton> singletonClass = (Class<StaticClassSingleton>) singleton.getClass();
        Class<StaticClassSingleton> singletonClass = (Class<StaticClassSingleton>) Class.forName("indi.xianglee94.designpattern.singleton.StaticClassSingleton");
        Constructor<StaticClassSingleton> c = singletonClass.getDeclaredConstructor(null);
        c.setAccessible(true);
        StaticClassSingleton singleton1 = c.newInstance();
        System.out.println(singleton1.getValue());
    }


}
