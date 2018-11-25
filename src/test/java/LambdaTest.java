import java.util.*;

public class LambdaTest {
    public static void main(String[] args) {
        Map<String,String> list = new HashMap<>();
        list.put("1","fd5sfdsaf");
        list.put("2","ffdsdsaf");
        list.put("3","fdsqwrvaf");
        list.put("4","fdseraf");
        list.put("6","fds5432af");
        List<String> s = new ArrayList<>();
        Optional.ofNullable(list.get("6")).ifPresent((i)->{s.add(i);});


    }
    public static void get(Integer a,Integer b){
        System.out.println(a);
    }

}

