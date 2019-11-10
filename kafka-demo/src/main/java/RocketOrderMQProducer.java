import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SunlandsProducer;
import org.apache.rocketmq.common.message.Message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RocketOrderMQProducer{
    static InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("rocketMQ-producer.properties");
    public static Properties properties = new Properties();
    static SunlandsProducer producer = new SunlandsProducer();
    {
        try{
            properties.load(in);
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public RocketOrderMQProducer() {
        //RocketMQServer NameServer地址
        producer.setNameServer(properties.getProperty("NameServer"));
        //在尚德基础架构平台控制台申请的唯一AppId
        producer.setAppId(properties.getProperty("AppId"));
        //在尚德基础架构平台控制台申请AppId时系统给定的AccessKey
        producer.setSecretKey(properties.getProperty("SecretKey"));
        //指定发送的消息类型,指定为有序
        producer.setMessageType(properties.getProperty("MessageType"));
        //在尚德基础架构平台控制台申请的发布msg的topic时,自动获取的ProducerGroup
        producer.setProducerGroup(properties.getProperty("ProducerGroup"));
        // 在尚德基础架构平台控制台申请的发布msg的topic,一级消息类型，通过Topic对消息进行分类
        producer.setTopic(properties.getProperty("Topic"));
        try {
            producer.init();
        }catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void send(String tag, String message, long timeout){
        //组装message对象
        //参数1：Message Topic
        //参数2：Message Tag, 二级消息类型，用来进一步区分某个 Topic 下的消息分类，方便Consumer指定过滤条件在服务器过滤
        //参数3：keys，业务方指定的唯一id，便于console查找，不设置不影响收发消息(默认为顺序消息),如果设置为不同，会分发到不同队列导致无序。
        //第三个参数业务方指定，并且这个函数接口就是这个参数对应的hash规则，通过这个hash规则返回对应的队列，这样队列固定达到有序效果
        //参数4：Body，任何二进制形式的数据，服务器不做任何干预，需要Producer与Consumer协商好一致的序列化和反序列化方式
        //也可灵活使用其他message构造器
        Message msg = new Message(properties.getProperty("Topic"), tag,  message.getBytes());
        try {
            producer.send(msg,timeout);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

   public void send(String tag, String message, int delayTimeLevel,long timeout){
        //组装message对象
        //参数1：Message Topic
        //参数2：Message Tag, 二级消息类型，用来进一步区分某个 Topic 下的消息分类，方便Consumer指定过滤条件在服务器过滤
        //参数3：keys，业务方指定的唯一id，便于console查找，不设置不影响收发消息(默认为顺序消息),如果设置为不同，会分发到不同队列导致无序。
        //第三个参数业务方指定，并且这个函数接口就是这个参数对应的hash规则，通过这个hash规则返回对应的队列，这样队列固定达到有序效果
        //参数4：Body，任何二进制形式的数据，服务器不做任何干预，需要Producer与Consumer协商好一致的序列化和反序列化方式
        //也可灵活使用其他message构造器
       //timeout默认值为3000
//       System.out.println("topic:"+properties.getProperty("Topic"));
        Message msg = new Message(properties.getProperty("Topic"), tag,  message.getBytes());
        //messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h 3h 24h 48h
        //1S 对应 level 1
        //依次往后推
        //48h  对应   level 21
        msg.setDelayTimeLevel(delayTimeLevel);
        try {
            producer.send(msg,timeout);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void shutdown(){
        producer.shutdown();
    }
}
