package pool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class MQProducerPool{
	private static final Log logger = LogFactory.getLog(MQProducerPoolManager.class);
	private final AtomicInteger counter = new AtomicInteger();
	private volatile GenericObjectPool<MQProducer> internalPool = null;
	
	final Properties kafkaProps;

	public MQProducerPool(Properties kafkaProps) {
		this.kafkaProps = kafkaProps;
	}
	
	public MQProducer getResource() throws Exception {
		MQProducer p = internalPool.borrowObject();
		p.pool = this;
		return p;
	}

	public void returnResource(MQProducer producer){
		internalPool.returnObject(producer);
	}
	
	public int mark() {
		int markId = counter.incrementAndGet();
		logger.info("mark, markId=" + markId);
		if(markId == 1) {
            MQProducerFactory factory = new MQProducerFactory(kafkaProps);

            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(200);
            config.setMaxIdle(100);
            //config.setMinIdle(20);

            config.setTimeBetweenEvictionRunsMillis(60000); //eviction check 60 seconds
            config.setMinEvictableIdleTimeMillis(600000);//idle timeout 10 minutes

            config.setTestOnBorrow(true); //校验连接配置是否改变
            config.setTestOnReturn(true); //校验连接配置是否改变
            internalPool = new GenericObjectPool<MQProducer>(factory, config);
			logger.info("initPool, markId=" + markId);
		}
		return markId;
	}
	
	public void release() {
		int markId = counter.decrementAndGet();
		logger.info("release, markId=" + markId);
		if(markId <= 0) {
			internalPool.close();
			internalPool = null;
			logger.info("destroy pool, markId=" + markId);
		}
	}
}
