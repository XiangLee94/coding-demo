package Java;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestSplit

{
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    static A a = new A();

    public static void main(String[] args) {
        for (int i = 0; i < 100000 ; i++) {
            final int  f = i;
                pool.execute(() ->{
                    String[] c = (new String[20]);
                    a.arr = c;
                });

            pool.execute(() ->{
                System.out.println(a.getArr());
            });
        }
    }

    static class A{

        public String [] arr = new String[10];

        public String[] getArr() {
            return arr;
        }

        public void setArr(String[] arr) {
            this.arr = arr;
        }
    }
}
