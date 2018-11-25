package thread;

public class TheadTest2 {
    public static void main(String[] args) throws InterruptedException {
        Runnable r = ()->{
            while(true){
                System.out.println("new thread running");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
