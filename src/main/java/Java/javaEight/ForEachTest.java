package Java.javaEight;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class ForEachTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(100000000);
        for(int i = 0; i< 100000000;i++){
            list.add(""+i);
        }
        long a = System.currentTimeMillis();
        for (String s:list ) {

        }


        long b = System.currentTimeMillis();
        list.forEach(s->{

        });
        long c = System.currentTimeMillis();

        list.stream().forEach(s->{

        });
        long d = System.currentTimeMillis();
        System.out.println("for"+(b-a)+"     forEach"+(c-b)+"        stream"+(d-c)+"");
        System.out.println("for"+(b-a)+"     forEach"+(c-b)+"        stream"+(d-c)+"");
    }
}
