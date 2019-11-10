package classloaderdemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassLoaderTest {
    //    Class c = ClassLoader.getSystemResource();
    public static void main(String[] args) {
        try{
            Class c = Class.forName("classloaderdemo.Dog",false,Thread.currentThread().getContextClassLoader());
            System.out.println("类名称：" + c.getName());
            System.out.println("是否为接口：" + c.isInterface());
            System.out.println("是否为基本类型：" + c.isPrimitive());
            System.out.println("是否为数组：" + c.isArray());
            System.out.println("父类：" + c.getSuperclass().getName());
            Constructor constructor = c.getConstructor(null);
            Object o = constructor.newInstance(null);
            c.getMethod("saySelf",null).invoke(o);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
