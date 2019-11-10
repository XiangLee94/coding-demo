package indi.xianglee94.designpattern.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class IneffectiveEnumSingleton {
    private IneffectiveEnumSingleton() {
    }

    public int getValue() {
        return 1;
    }

    private enum InnerSingletonEnum {
        INSTANCE(new IneffectiveEnumSingleton());
        private IneffectiveEnumSingleton ineffectiveEnumSingleton;

        InnerSingletonEnum(IneffectiveEnumSingleton ineffectiveEnumSingleton) {
            this.ineffectiveEnumSingleton = ineffectiveEnumSingleton;
        }

        public IneffectiveEnumSingleton getInstance(){
            return INSTANCE.ineffectiveEnumSingleton;
        }
    }

    public static IneffectiveEnumSingleton getInstance() {
        return InnerSingletonEnum.INSTANCE.getInstance();
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        reflectCrack();
    }

    public static void reflectCrack() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
//        IneffectiveEnumSingleton singleton = getInstance();
//        Class<StaticClassSingleton> singletonClass = (Class<StaticClassSingleton>) singleton.getClass();
        Class<IneffectiveEnumSingleton> singletonClass = (Class<IneffectiveEnumSingleton>) Class.forName("indi.xianglee94.designpattern.singleton.IneffectiveEnumSingleton");
        Constructor<IneffectiveEnumSingleton> c = singletonClass.getDeclaredConstructor(null);
        c.setAccessible(true);
        IneffectiveEnumSingleton singleton1 = c.newInstance();
        System.out.println(singleton1.getValue());
    }
}
