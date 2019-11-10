package pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Properties;

public class MQProducerFactory implements PooledObjectFactory<MQProducer> {

	private final Properties kafkaProps;
    public MQProducerFactory(Properties props) {
    	this.kafkaProps = props; 
    }
    
	@Override
	public PooledObject<MQProducer> makeObject() throws Exception {
		MQProducer producer = new MQProducer(kafkaProps);
		return new DefaultPooledObject<>(producer);
	}

	@Override
	public void destroyObject(PooledObject<MQProducer> p) throws Exception {
		MQProducer producer = p.getObject();
		if(producer!=null)
			producer.destroy();
	}

	@Override
	public boolean validateObject(PooledObject<MQProducer> p) {
		MQProducer h = p.getObject();
		return !h.closed();
	}

	@Override
	public void activateObject(PooledObject<MQProducer> p) throws Exception {
	}

	@Override
	public void passivateObject(PooledObject<MQProducer> p) throws Exception {
	}

}
