package redis;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class JedisCluster {

	private static Lock lock = new ReentrantLock();

	private static Set<HostAndPort> hostAndPortSet = new HashSet<>();
	private static JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	public static redis.clients.jedis.JedisCluster jedisCluster ;
//	static {
//		hostAndPortSet.add(new HostAndPort("192.168.0.80",7001));
//		hostAndPortSet.add(new HostAndPort("192.168.0.80",7002));
//		hostAndPortSet.add(new HostAndPort("192.168.0.80",7003));
//		hostAndPortSet.add(new HostAndPort("192.168.0.80",7004));
//		hostAndPortSet.add(new HostAndPort("192.168.0.80",7005));
//		hostAndPortSet.add(new HostAndPort("192.168.0.80",7006));
//		jedisPoolConfig.setMaxIdle(200);
//		jedisPoolConfig.setMaxTotal(400);
//		jedisPoolConfig.setMaxWaitMillis(10000);
//		jedisPoolConfig.setTestOnBorrow(true);
//		jedisCluster = new JedisCluster(hostAndPortSet,10000,5000,3,"eaglegogo",jedisPoolConfig);
//	}

	static {
		hostAndPortSet.add(new HostAndPort("172.16.116.36",6379));
		hostAndPortSet.add(new HostAndPort("172.16.116.76",6379));
//		jedisPoolConfig.setMaxIdle(200);
//		jedisPoolConfig.setMaxTotal(400);
//		jedisPoolConfig.setMaxWaitMillis(10000);
//		jedisPoolConfig.setTestOnBorrow(true);
		jedisCluster = new redis.clients.jedis.JedisCluster(hostAndPortSet,10000,5000,3,"zaq12wsx",jedisPoolConfig);
	}

	
	public String getValueByKey(String key) {
		String v = null;
		try {
			v = jedisCluster.get(key);
			if(v != null) {
				return v;
			}
		} catch(Exception e) {
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObjectByJson(String key,Class<T> t){
		String json = getValueByKey(key);
		return JSONObject.parseObject(json,t);
	}
	
	
	
	public  void setValue( String key, String value, int cacheSeconds) {
		try {
			if(StringUtils.isEmpty(key)){
				return;
			}
			jedisCluster.set(key, value.length() > 0 ? value : "");
			jedisCluster.expire(key, cacheSeconds);//seconds, 2 call ??
		} catch(Exception e) {
		}
	}
	
	public  void listPush( String key, List<String> list, int cacheSeconds) {
		lock.lock();  
		try {
			if(jedisCluster.exists(key.getBytes()) || null == list || list.size() == 0){
				return;
			}
			
			Collections.reverse(list);
			String[] strings = new String[list.size()];
			list.toArray(strings);
			jedisCluster.lpush(key,strings);
			jedisCluster.expire(key, cacheSeconds);//seconds, 2 call ??
			Collections.reverse(list);
		} catch(Exception e) {
		}finally{
			 lock.unlock();
		}
	}

	public  List<String> getList(String key) {
		List<String> result = null;
		try {
			result = jedisCluster.lrange(key, 0, -1);
			
		} catch(Exception e) {
		}
		return result;
	}
	public  <T> void setObject( String key, T object, int cacheSeconds) {
		try {
			if(jedisCluster.exists(key.getBytes()) || null == object){
				return;
			}
			jedisCluster.set(key.getBytes(),CommonUtil.serialize(object));
			jedisCluster.expire(key.getBytes(), cacheSeconds);//seconds, 2 call ??
		} catch(Exception e) {
		}
	}
	public  <T> T getObject( String key,Class<T> t) {
		T object = null ;
		try {
			  byte[] bs = jedisCluster.get(key.getBytes());
			  object = (T) CommonUtil.deserialize(bs);
		} catch(Exception e) {
		}
		return object;
	}
	
	public  <T> void setObjects( String key, List<T> objects, int cacheSeconds) {
		lock.lock();  
		try {
			if(jedisCluster.exists(key.getBytes()) || null == objects || objects.size() == 0){
				return;
			}
			for(T item:objects){
				jedisCluster.lpush(key.getBytes(),CommonUtil.serialize(item));
			}
			jedisCluster.expire(key.getBytes(), cacheSeconds);//seconds, 2 call ??
		} catch(Exception e) {
		}finally{
			lock.unlock();
		}
	}
	public  <T> List<T> getObjects( String key,Class<T> t) {
		List<T> list = new ArrayList<T>();
		try {
			
			 long count = jedisCluster.llen(key.getBytes());
			 for(long i=0;i<count;i++){
				 byte[] bs = jedisCluster.lindex(key.getBytes(), i);
				T object = (T) CommonUtil.deserialize(bs);
				list.add(object);
			 }
			
		} catch(Exception e) {
		}
		return list;
	}
	public  <T> void setAdd( String key, List<T> objects, int cacheSeconds) {
		lock.lock();  
		try {
			if(jedisCluster.exists(key.getBytes()) || null == objects || objects.size() == 0){
				return;
			}
			for(T item:objects){
				jedisCluster.sadd(key.getBytes(),CommonUtil.serialize(item));
			}
			jedisCluster.expire(key.getBytes(), cacheSeconds);//seconds, 2 call ??
		} catch(Exception e) {
		}finally{
			lock.unlock();
		}
	}
	public  <T> List<T> getSet( String key,Class<T> t) {
		List<T> list = new ArrayList<T>();
		try {
			Set<byte[]> cards = jedisCluster.smembers(key.getBytes());
			for(byte[] card: cards){
				T object = (T) CommonUtil.deserialize(card);
				list.add(object);
			}
			
		} catch(Exception e) {
		}
		return list;
	}
	
	public  void sStrs( String key, List<String> list, int cacheSeconds) {
		lock.lock();  
		try {
				if(jedisCluster.exists(key) || null == list || list.size() == 0){
					return;
				}
				String[] strings = new String[list.size()];
				list.toArray(strings);
				jedisCluster.sadd(key,strings);
				jedisCluster.expire(key, cacheSeconds);//seconds, 2 call ??
			}catch(Exception e) {
		}finally{
			lock.unlock();
		}
	}
	public  List<String> getSetStrs( String key) {
		List<String> list = new ArrayList<String>();
		try {
				Set<String> cards = jedisCluster.smembers(key);
				for(String card: cards){
					list.add(card);
				}
		} catch(Exception e) {
		}
		return list;
	}

	public static void main(String[] args) {
		OrgInfo orgInfo = new OrgInfo();
		orgInfo.setOrgId("11");
		orgInfo.setOrgName("jdisa");
		JedisCluster.jedisCluster.set("test123",JSONObject.toJSONString(orgInfo));

		JedisCluster.jedisCluster.get("test123");

		OrgInfo o =JSONObject.parseObject(JedisCluster.jedisCluster.get("test123"),OrgInfo.class);

		System.out.println(o.getOrgName());
	}

}
