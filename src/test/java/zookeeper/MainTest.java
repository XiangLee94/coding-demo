package zookeeper;

import zookeeper.CanalClientWatcher;
import zookeeper.Observer;



public class MainTest extends Observer {


    private CanalClientWatcher canalClientWatcher  = CanalClientWatcher.create("192.168.0.80","2181");

    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        MainTest mainTest = new MainTest();
        mainTest.canalClientWatcher.attach(mainTest);



        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("jvm 运行结束");
        }));
        while(true){
            if(mainTest.canalClientWatcher.isLeader()){
                System.out.println("我是 leader");
                while(true){
                    System.out.println("Running .............");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                synchronized (lock){
                    lock.wait();
                }
            }
        }



    }

    @Override
    public void update() {
        System.out.println("安排上了");
        System.out.println("是不是leader"+canalClientWatcher.isLeader());
        synchronized (lock){
            lock.notify();
        }
    }
}
