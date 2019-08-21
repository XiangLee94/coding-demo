package indi.xianglee94.javacode.java8.streamdemo;

import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) throws InterruptedException {
        testGroupBy();

    }

    public static void testSpeed() throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new Date().toString());
            Thread.sleep(1);
        }
        list.stream().forEach(i -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
        });
        System.out.println("over ============= ");
    }

    public static void testGroupBy() {
        List<CanalTableConfig> canalTableConfigs = new ArrayList<>();
        CanalTableConfig c1 = new CanalTableConfig("dragnet", "drag_opportunity", "user_operation", "uo");
        CanalTableConfig c2 = new CanalTableConfig("dragnet", "drag_opportunity", "eagle_eye", "ee");
        CanalTableConfig c3 = new CanalTableConfig("ent_portal_prod", "t_user_info", "user_operation", "uo");
        CanalTableConfig c4 = new CanalTableConfig("dragnet", "drag_consultant_wechat_config", null, "uo");
        canalTableConfigs.add(c1);
        canalTableConfigs.add(c2);
        canalTableConfigs.add(c3);
        canalTableConfigs.add(c4);
        Map<String, Map<String, String>> map = canalTableConfigs.stream().collect(Collectors.groupingBy(CanalTableConfig::getTable, Collectors.groupingBy(CanalTableConfig::getBusiness,Collectors.collectingAndThen(Collectors.toList(), c->(String)c.get(0).getTopic()))));
        String topic = Optional.ofNullable(map).map(i->i.get("drag_consultant_wechat_config")).map(j->j.get("dk")).orElse("default");
        Optional.ofNullable(map).map(i->i.get("drag_opportunity")).filter(m->m.containsKey("dk")).map(j->j.put("dk","nima"));
        System.out.println(JSONObject.toJSONString(map));
        System.out.println(topic);

    }
}

class CanalTableConfig {
    private String instance;
    private String table;
    private String topic;
    private String business;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public CanalTableConfig(String instance, String table, String topic, String business) {
        this.instance = instance;
        this.table = table;
        this.topic = topic;
        this.business = business;
    }
}