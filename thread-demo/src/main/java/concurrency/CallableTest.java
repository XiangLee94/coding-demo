package concurrency;

import java.util.concurrent.*;

public class CallableTest {
    public static final ExecutorService executorPool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        useExcutor();
        executorPool.shutdown();
    }

    public static void useThread(){
        long a = System.currentTimeMillis();
        TaskResult taskResult = new TaskResult();
        Callable<TaskResult> getData1 = new CallableData1();
        Callable<TaskResult> getData2 = new CallableData2();
        FutureTask<TaskResult> data1 = new FutureTask<>(getData1);
        FutureTask<TaskResult> data2 = new FutureTask<>(getData2);
        new Thread(data1).start();
        new Thread(data2).start();
        try {
            taskResult.setData1(data1.get(4, TimeUnit.SECONDS).getData1());
            taskResult.setData2(data2.get(4, TimeUnit.SECONDS).getData2());
            taskResult.setStatus("success");
        } catch (Exception e) {
            e.printStackTrace();
            taskResult.setStatus("fail");
        }
        System.out.println(taskResult.toString());
        System.out.println(System.currentTimeMillis()-a);
    }

    public static void useExcutor(){
        long a = System.currentTimeMillis();
        TaskResult taskResult = new TaskResult();
        Callable<TaskResult> getData1 = new CallableData1();
        Callable<TaskResult> getData2 = new CallableData2();
        FutureTask<TaskResult> data1 = new FutureTask<>(getData1);
        FutureTask<TaskResult> data2 = new FutureTask<>(getData2);
        executorPool.submit(data1);
        executorPool.submit(data2);
        data1.cancel(true);
        while (true){
            if(data1.isDone() && data2.isDone()){
                try {
                    taskResult.setData1(data1.get(6, TimeUnit.SECONDS).getData1());
                    taskResult.setData2(data2.get(6, TimeUnit.SECONDS).getData2());
                    taskResult.setStatus("success");
                } catch (Exception e) {
                    e.printStackTrace();
                    taskResult.setStatus("fail");
                }
                System.out.println(taskResult.toString());
                System.out.println(System.currentTimeMillis()-a);
                break;
            }
        }
    }

}

class CallableData1 implements Callable<TaskResult>{

    @Override
    public TaskResult call() throws Exception {
        TaskResult taskResult = new TaskResult();
        System.out.println("set data1 start");
        taskResult.setData1("data1");
        Thread.sleep(5000);
//        int a = 1/0;
        System.out.println("set data1 end");
        return taskResult;
    }
}

class CallableData2 implements Callable<TaskResult>{

    @Override
    public TaskResult call() throws Exception {
        TaskResult taskResult = new TaskResult();
        System.out.println("set data2 start");
        taskResult.setData2("data1");
        Thread.sleep(3000);
        System.out.println("set data2 end");
        return taskResult;
    }
}

class TaskResult{
    private String status;
    private String data1;
    private String data2;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "status='" + status + '\'' +
                ", data1='" + data1 + '\'' +
                ", data2='" + data2 + '\'' +
                '}';
    }
}