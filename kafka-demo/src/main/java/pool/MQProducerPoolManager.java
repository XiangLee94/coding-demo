package pool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class MQProducerPoolManager {
	private static final Log logger = LogFactory.getLog(MQProducerPoolManager.class);
	private static final String CONF_BOOTSTRAP_SERVERS = "bootstrap.servers";

	private static Properties extractPoolProps(Configuration conf) {
		if(conf != null) {
			Properties poolProps = new Properties();
			
			for(Map.Entry<String, String> entry : conf) {
				if(entry.getKey()!=null && entry.getKey().startsWith("pool.")) {
					String newKey = entry.getKey().substring("pool.".length());
					poolProps.put(newKey, entry.getValue());
				}
			}
			return poolProps;
		}
		
		return null;
	}
	
	private static final Properties commonProps = new Properties();
	private static final Map<String, MQProducerPool> pools =  new ConcurrentHashMap<String, MQProducerPool>();
	private static final Object locker = new Object();
	private MQProducerPoolManager() {}
	static {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("producer.properties");
		try {
			commonProps.load(in);
			logger.info("producer.properties == >"+commonProps);
			
			MQProducerPool commonPool = new MQProducerPool(commonProps);
			pools.put(commonProps.getProperty(CONF_BOOTSTRAP_SERVERS), commonPool);
			in.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e.getCause());
		}
	}
	
	public static MQProducerPool loadProducerPool(Configuration conf) {
        Properties overrideProps = extractPoolProps(conf);
		String brokers = (overrideProps == null) ? null : overrideProps.getProperty(CONF_BOOTSTRAP_SERVERS);
		if(brokers==null || brokers.length()==0) {
		    brokers = commonProps.getProperty(CONF_BOOTSTRAP_SERVERS);
			logger.warn("bootstrap.servers not provided, common pool will be used, common props => " + commonProps);
		}
		
		MQProducerPool pool = pools.get(brokers);
		if(pool != null) {
			logger.warn("override pool already loaded, props => " + pool.kafkaProps);
			pool.mark();
			return pool;
		}
		else {
			return load(brokers, overrideProps);
		}
	}
	
	private static MQProducerPool load(String brokers, Properties overrideProps) {
		synchronized(locker) {
			MQProducerPool pool = pools.get(brokers);
			if(pool != null) {
				pool.mark();
				return pool;
			}
			
			Properties newProps = new Properties();
			newProps.putAll(commonProps);
			newProps.putAll(overrideProps);
			
			logger.warn("new pool is loaded, new props => " + newProps);
			pool = new MQProducerPool(newProps);
			pools.put(brokers, pool);
			
			pool.mark();
			return pool;
		}
	}
	
}
