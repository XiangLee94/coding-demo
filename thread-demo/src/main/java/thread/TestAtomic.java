package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class TestAtomic

{
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    static AtomicReference<A> a = new AtomicReference<>(new A());

    public static void main(String[] args) {
        for (int i = 0; i < 1000 ; i++) {
            final int  f = i;
            pool.execute(() ->{
                A aa = new A();
                A s = a.get();
                aa.setA(s.getA()+1);
                aa.setB(s.getB()+1);
                a.getAndSet(aa);
                System.out.println(a.get().getA()+""+a.get().getB());
            });
        }
    }
    static class A{
        int a = 0;
        int b = 0;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }
}
