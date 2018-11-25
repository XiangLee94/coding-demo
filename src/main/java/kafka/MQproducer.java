package kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.*;

public class MQproducer {

    private static final Logger logger = LoggerFactory
            .getLogger(MQproducer.class);
    public static final ExecutorService executorPool = Executors.newFixedThreadPool(10);
    private KafkaProducer<String, String> producer;
    private String topic;

    public MQproducer() {
        try {
            if (producer != null) {
                producer.close();
            }
            InputStream in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("producer.properties");
            Properties props = new Properties();
            props.load(in);
            System.out.println(in);
            System.out.println(props.toString());
            topic = props.getProperty("kafka.producer.topics.eagle.spark");
            producer = new KafkaProducer<>(props);
            logger.info("初始化KafkaProducer成功.");
        } catch (IOException e) {
            throw new RuntimeException("读取kafka配置文件失败:", e);
        }
    }

    public void send(String topic, String message) {
        try {

         producer.send(new ProducerRecord<String, String>(topic, message));

            logger.info(topic + "|" + message);
        } catch (Exception e) {
            logger.error("失败:" + e.getMessage());
        }
    }

    public void send(String message) {
        send(topic, message);
    }

    @Override
    public void finalize() {
        if (producer != null) {
            producer.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MQproducer m = new MQproducer();
        long a = System.currentTimeMillis();
        for (int i = 0;i<10000;i++) {
            Thread.sleep(1000);
            if(i%7 == 0)
            m.send(i+"aaaa");
            else
                m.send(i+"bbbb");
        }
        //Thread.sleep(1000);
        long b = System.currentTimeMillis();
        System.out.println("小号时间"+(b-a));


    }
    public void sendNoGet() throws InterruptedException {
        MQproducer m = new MQproducer();
        long a = System.currentTimeMillis();
        for (int i = 0;i<100;i++) {
            Thread.sleep(1000);
            if(i%7 == 0)
            m.send(i+"aaaa");
            else
                m.send(i+"bbbb");
        }
        //Thread.sleep(1000);
        long b = System.currentTimeMillis();
        System.out.println("小号时间"+(b-a));
    }

    public void sendAndGet(){
        MQproducer m = new MQproducer();
        long a = System.currentTimeMillis();
        try {

            Future f = m.producer.send(new ProducerRecord<String, String>(m.topic, ""));

            logger.info(m.topic + "|" + "");
        } catch (Exception e) {
            logger.error("失败:" + e.getMessage());
        }
        long b = System.currentTimeMillis();
        System.out.println("小号时间"+(b-a));
    }
}
