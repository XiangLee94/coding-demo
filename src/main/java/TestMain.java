
import jdk.nashorn.internal.ir.IdentNode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class TestMain {
    private Integer a = 0;

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    int anInt;
    private static Comparator<TestMain> idComparator1 = (t1, t2) -> t1.getA() - t2.getA();
    static  Queue<TestMain> jobQueue = new PriorityQueue<>(idComparator1);
    public static void main(String[] args) {
        TestMain testMain = new TestMain();
        TestMain testMain1 = new TestMain();
        jobQueue.add(testMain);
        jobQueue.add(testMain1);
        TestMain t = jobQueue.poll();
        t.setA(-2);
        jobQueue.add(t);
        TestMain t2 = jobQueue.poll();
        System.out.println(t2.getA());

    }

    public static void test(){
        List<String> strings = new ArrayList<>();
        for(int i = 0 ; i< 10000000 ;i ++){
            strings.add(String.valueOf(i));
        }
        long st1 = System.currentTimeMillis();
        for(String a :strings){
            if(a.contains("s"))
                System.out.println(a);
        }
        long st2 = System.currentTimeMillis();
        long st3 = System.currentTimeMillis();
        System.out.println(st2 -st1);
        System.out.println(st3 -st2);
    }

    public static String getDatrStr(){
        LocalDate today = LocalDate.now();
        LocalDate monthFirst = today.withDayOfMonth(1);
        return  monthFirst.toString();
    }
//    public static String
    public static String getYesDayMonthFirst(){
        LocalDate result = LocalDate.now().plusDays(-1).withDayOfMonth(1);
        return result.toString() + " 00:00:00";
    }
    public static String  getNowMile(){
         Clock.systemDefaultZone().toString();
        return Clock.systemUTC().toString();
    }

}

interface Fin{
    int  cal(int a,int b);
}

