package indi.xianglee94.javacode;

import java.util.Arrays;
import java.util.HashSet;

public class Test {
    public static void main(String[] args) {


        String label = "a=";
        String[] labelArr = label.split("=");
        System.out.println("");
    }

    private static void testSwitch() {
        String str = "dsa";
        switch (str) {
            case "nihao":
                System.out.println("nihao");
                break;
            case "buhao":
                System.out.println("buhao");
                break;
            case "shenme":
                System.out.println("shenme");
                break;
            case "nima":
                System.out.println("nima");
                System.out.println("nima");
            default:
                System.out.println("nothing");
        }

    }


    private static void testContainsAll(){
        HashSet<String> set  = new HashSet(Arrays.asList("pv","submit"));
        HashSet<String> set1  = new HashSet(Arrays.asList("pv","submit","dsa"));
        System.out.println(set.containsAll(set1));
    }
}
