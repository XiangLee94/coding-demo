package zookeeper;

import java.util.ArrayList;
import java.util.List;

public abstract class Observer {
    protected CanalClientWatcher watcher;
    public abstract void update();
}