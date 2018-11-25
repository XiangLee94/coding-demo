package thread;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ThreadTest {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<String>();
        List<String> list = new LinkedList<String>();

        Thread s1 = new Consumer(queue);
        Thread s2 = new Producer(queue);
        s1.start();
        s2.start();
        System.out.println("主线程结束");
    }

}

class Consumer extends Thread {

    private Queue<String> queue;

    public Consumer(Queue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.notify();
                try {
                    queue.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String con = queue.poll();
                System.out.println("comsume            " + con);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class Producer extends Thread {
    private Queue<String> queue;

    public Producer(Queue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        int a = 0;
        synchronized (queue) {

            while (a < 1000) {
                if(queue.size() > 10){
                    queue.notify();
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                a++;
                String add = "pro" + a;
                queue.add(add);
                System.out.println("peoduce         " + add);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

