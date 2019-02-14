package concurrency.util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args)  {
        test();
    }

    /*
    * 思路类似于es入库程序
    * 初始化时指定数量，这些线程执行到await后才开始执行每个代码await的之后的代码
    * 也可以设置超时策略
    *
    *
    *
    *
    * */
    public static void test()  {
        //3个任务到达berrier状态就继续
        CyclicBarrier barrier = new CyclicBarrier(3);
        for (int i = 0; i < 9 ; i++) {
            int finalI = i;
            new Thread(()->{
                try {
                    System.out.println("线程"+Thread.currentThread().getName()+"开始准备");
                    Thread.sleep(1000* finalI);
                    barrier.await();
                    System.out.println("线程"+Thread.currentThread().getName()+"执行之后的任务");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        new Thread(()->{
            try {
                barrier.await();
                System.out.println("三个线程准备完成了，就走这里");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();




    }

}

