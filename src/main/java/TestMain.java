
import jdk.nashorn.internal.ir.IdentNode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestMain {
    private Integer a = null;

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public static void main(String[] args) {
        long st2 = System.currentTimeMillis();
        String source = "t_action_info_20180701";
        String regex = "t_action_info_201807";
        String replacement = "t_action_info";
        String target = source.replaceFirst(regex,replacement);
        System.out.println(target);

        long st3 = System.currentTimeMillis();

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

