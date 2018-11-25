package redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class JedisClient {

   static Jedis jedis = new Jedis("172.16.116.36",6379);

    public static void main(String[] args) {

        Set sentinels = new HashSet();
        sentinels.add(new HostAndPort("192.168.0.80", 7001).toString());
        sentinels.add(new HostAndPort("192.168.0.80", 7002).toString());
        JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
        Jedis jedis = sentinelPool.getResource();
        jedis.auth("eaglegogo");
        jedis.set("laoli1","11");
    }
}
