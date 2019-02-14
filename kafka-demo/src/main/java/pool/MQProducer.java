package pool;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.internals.RecordAccumulator;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.concurrent.Future;

public class MQProducer implements Closeable {

    public MQProducer(Properties properties) {
        this.internal = new KafkaProducer<>(properties);
    }

    volatile MQProducerPool pool;

    private final KafkaProducer<String, String> internal;

    public Future<RecordMetadata> send(ProducerRecord<String, String> record, Callback callback) {
        return internal.send(record, callback);
    }

    @Override
    public void close() {
        if(pool!=null && !closed()) {
            pool.returnResource(this);
        }
    }

    boolean closed() {
        try {
            Field field = KafkaProducer.class.getDeclaredField("accumulator");

            field.setAccessible(true);
            RecordAccumulator accu = (RecordAccumulator)field.get(internal);

            Field field2 = RecordAccumulator.class.getDeclaredField("closed");
            field2.setAccessible(true);
            return (boolean)field2.get(accu);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    void destroy() {
        internal.close();
    }
}
