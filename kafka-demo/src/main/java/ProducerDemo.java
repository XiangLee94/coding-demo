import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.avro.data.Json;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerDemo {

    private static long failcount = 0;


    Properties properties ;
    private String server;
    private KafkaProducer<String, String> kafkaProducer;
    private static final Callback callBack = new Callback() {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e!=null){
                System.out.println("exception message is: " + e);
            }
            System.out.println("recordMetadata=============="+JSONObject.toJSONString(recordMetadata));
        }
    };

    public ProducerDemo(String server){
        this.server = server;
        createProducer(server);
    }


    public void createProducer(String server){
        /**1.创建生产者必要的三个配置
         * ① kafka broker地址,不需要全配,找到一个即可根据broker找到其他broker
         * ②.key,value发送到网络的时候需要是字节数组,选择key,value的序列化器
         */
        properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.BUFFER_MEMORY_CONFIG,"16384");
        /**2.创建生产者
         * 发送消息的三种方式:
         * ①fire-and-forget 发送并忘记,会自动尝试重发,有时候会丢消息
         * ②同步发送send(),返回Future,调用get()方法进行等待
         * ③异步发送send(),指定一个回调函数,服务器响应时调用
         */
        try {
           this.kafkaProducer= new KafkaProducer<>(properties);
        }catch (Exception e){
        }
    }

    //1.fire-and-forget,发送完消息就不管了,不管future对象就不会阻塞.
    public void sendAndForget(String topic ,String keyForPartion,String message){
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, keyForPartion, message);
        kafkaProducer.send(producerRecord);
    }
    //2.同步等待执行完毕,Future对象中包含RecordMetaDtata.主题.分区,记录在分区的偏移量,写入失败会有一个错误信息
    public RecordMetadata sendAndSycWait(String topic , String key, String message){
        RecordMetadata recordMetadata = null;
        try{
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, message);
            Future<RecordMetadata> future = kafkaProducer.send(producerRecord);
            recordMetadata = future.get();
            //recordMetadata.topic() recordMetadata.offset() recordMetadata.partition() recordMetadata.timestamp()
        }catch (Exception e){
        }
        return recordMetadata;
    }
    //异步发送消息,好处是可以不需要等待网络的延迟
    public void sendAndAsycCallBack(String topic , String keyForPartion, String message){
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, keyForPartion, message);
        kafkaProducer.send(producerRecord,new MyCallback(topic,keyForPartion,message));
    }

    private synchronized void reBuildProducer(){
        createProducer(server);
    }

    public void close(){
        kafkaProducer.close();
    }


    class MyCallback implements  Callback{
        private String topic;

        private String key;

        private String message;

        public MyCallback(String topic, String key, String message) {
            this.topic = topic;
            this.key = key;
            this.message = message;
        }

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if ( !(e == null)){
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, message);
                System.out.println("send fail");
//                kafkaProducer.send(producerRecord,new MyCallback(topic,key,message));
            }
        }
    }
}
