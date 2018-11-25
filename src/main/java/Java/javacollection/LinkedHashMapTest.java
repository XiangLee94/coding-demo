package Java.javacollection;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapTest {
    public static void main(String[] args) {
        Map<String,String> map = new LinkedHashMap();
        map.put( "5" ,"111");
        map.put( "4" ,"222");
        map.put( "3" ,"333");
        map.put( "7" ,"444");
        map.put( "1" ,"555");
        for( String entry   : map.keySet()){
            System.out.println(map.get(entry));
        }
    }
}
