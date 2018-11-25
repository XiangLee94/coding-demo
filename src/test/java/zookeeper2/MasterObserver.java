package zookeeper2;

public class MasterObserver extends Observer {

    private Object lock;
    public MasterObserver(CanalClientWatcher watcher,Object lock){
        this.watcher = watcher;
        this.watcher.attach(this);
        this.lock = lock;
    }

    @Override
    public void update() {
        System.out.println("安排上了");
        synchronized (lock){
            lock.notify();
        }
    }
}
