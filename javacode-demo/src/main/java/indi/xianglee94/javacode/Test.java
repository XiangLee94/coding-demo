package indi.xianglee94.javacode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        JSONObject j = JSON.parseObject("{\n" +
                "  \"tblName\": \"event_log.dc_api_access_log\",\n" +
                "  \"rowkey\": \"%{timestamp}\",\n" +
                "  \"seqNum\": 1560766295000,\n" +
                "  \"etlTime\": 1560766295000,\n" +
                "  \"opr\": \"INSERT\",\n" +
                "  \"jsonData\": \"\\\"{\\\"log_str\\\":\\\"GETdc-webapi-workbenchnull3http://datacenter-workbench/datacenter-workbench/common/getOrderInfo2019-06-17{fields\\u003dcall_seat_em, serialNo\\u003dENT20190616002237_1}\\\",\\\"log_create_time\\\":\\\"2019-06-17 11:07:00\\\",\\\"request_method\\\":\\\"GET\\\",\\\"project_name\\\":\\\"dc-webapi-workbench\\\",\\\"username_or_token\\\":\\\"null\\\",\\\"reponse_millisecond\\\":\\\"3\\\",\\\"request_url\\\":\\\"http://datacenter-workbench/datacenter-workbench/common/getOrderInfo\\\",\\\"logstash_send_time\\\":\\\"2019-06-17\\\",\\\"request_param\\\":\\\"{fields\\u003dcall_seat_em, serialNo\\u003dENT20190616002237_1}\\\"}\\\"\"\n" +
                "}");
       System.out.println( j.get("jsonData"));
    }

//    private static void testSwitch() {
//        String str = "dsa";
//        switch (str) {
//            case "nihao":
//                System.out.println("nihao");
//                break;
//            case "buhao":
//                System.out.println("buhao");
//                break;
//            case "shenme":
//                System.out.println("shenme");
//                break;
//            case "nima":
//                System.out.println("nima");
//                System.out.println("nima");
//            default:
//                System.out.println("nothing");
//        }
//
//    }


    private static void testContainsAll(){
    }
}
