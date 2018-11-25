package Java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

public class Test {
    public static final ExecutorService executorPool = Executors.newFixedThreadPool(1000);
    static  Map<String, String> map = new HashMap<>();
    static {
        for (int i = 0; i <=10000 ; i++) {
            map.put(i+"",i+"");
        }
    }


    public static void main(String[] args) {
        long a = System.currentTimeMillis();
        List<FutureTask<Result>> futureTasks = new ArrayList<>();
        for (String key : map.keySet()) {
            Callable<Result> callable = new MyCallable(key);
            FutureTask<Result> futureTask = new FutureTask<>(callable);
            executorPool.submit(futureTask);
            futureTasks.add(futureTask);
        }
        for (FutureTask f : futureTasks) {
            try {
                int failCount = 0;
                Result r = (Result)f.get();
                System.out.println(r.getTable()+r.getDone());
                boolean isSuccess = r.getDone();
                while(!isSuccess && failCount <5){
                    Callable<Result> callable = new MyCallable(r.getTable());
                    FutureTask<Result> futureTask = new FutureTask<>(callable);
                    executorPool.submit(futureTask);
                    failCount ++;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long b = System.currentTimeMillis();

        System.out.println("时长"+(b-a));
    }
    static  class MyCallable implements Callable<Result> {
        String table;

        public MyCallable(String table) {
            this.table = table;
        }

        @Override
        public Result call() {
            Result result = new Result();
            try {
                result.setTable(table);
                result.setDone(true);
                int j = 0;
//                for (int i = 0; i <1000000000 ; i++) {
//                    j += i;
//                }

                File file = new File("E:/config.properties");
                InputStream in = null;
                try {
                    System.out.println("以字节为单位读取文件内容，一次读一个字节：");
                    // 一次读一个字节
                    in = new FileInputStream(file);
                    int tempbyte;
                    while ((tempbyte = in.read()) != -1) {
                       System.out.write(tempbyte);
                    }
                    in.close();
                } catch (IOException e) {

                }

                } catch (Exception e) {
                e.printStackTrace();
                result.setDone(false);
            }
            System.out.println(map.get(table));
            return result;
        }
    }
}

class Result {
    public String table;
    public Boolean done;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}

