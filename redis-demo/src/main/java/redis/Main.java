package redis;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        Thread a = new Thread(runnable);
        Thread b = new Thread(runnable);
        Thread c = new Thread(runnable);
        Thread d = new Thread(runnable);
        a.start();
        b.start();
        c.start();
        d.start();
    }
}
