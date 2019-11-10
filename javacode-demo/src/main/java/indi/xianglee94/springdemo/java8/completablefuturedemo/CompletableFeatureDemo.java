package indi.xianglee94.javacode.java8.completablefuturedemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFeatureDemo {
    public static void main(String[] args) {
        try {
            test1();
        }catch (Exception e){
        }
    }

    public static void test1() throws ExecutionException, InterruptedException {
        long op = System.currentTimeMillis();
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("step1");
        });
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("step2");
        });
        CompletableFuture<Void> f3 = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("step3");
        });
        f1.get();
        f2.get();
        f3.get();
        long ed = System.currentTimeMillis();
        System.out.println(ed-op);
    }
}
