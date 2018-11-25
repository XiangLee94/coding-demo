package zookeeper2;

public abstract class Observer {
    protected CanalClientWatcher watcher;
    public abstract void update();
}