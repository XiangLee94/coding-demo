package indi.xianglee94.springdemo.java8.streamdemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StreamDemo {
    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();
        for(int i = 0 ; i < 1000; i ++){
            list.add(new Date().toString());
            Thread.sleep(1);
        }
        list.stream().forEach(i->{
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
        });
        System.out.println("over ============= ");
    }
}
