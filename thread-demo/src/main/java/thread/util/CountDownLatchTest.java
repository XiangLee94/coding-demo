package thread.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10);
        for(int i = 1 ;i <=  10 ;i ++){
            CompletableFuture.runAsync(()->{
                System.out.println("----------------------"+Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }

        try {
            System.out.println("在这里阻塞");
            latch.await();
            System.out.println("其他线程跑完，到我了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
