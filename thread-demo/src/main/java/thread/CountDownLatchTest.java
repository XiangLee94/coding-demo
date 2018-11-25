package thread;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10);
        for(int i = 1 ;i <=  10 ;i ++){
            new Thread((new Runnable() {
                @Override
                public void run() {

                    System.out.println("----------------------"+Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();

                }
            })).start();

        }
//        try {
//            latch.await();
//            System.out.println("三个线程跑完，到我了");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



    }
}
