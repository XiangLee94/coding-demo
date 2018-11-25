package zookeeper2;


public class MainTest {


    private static CanalClientWatcher canalClientWatcher  = CanalClientWatcher.create("192.168.0.80","2181");

    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        new MasterObserver(canalClientWatcher,lock);

        while(true){
            if(canalClientWatcher.isLeader()){
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

}
