package redis;

public class PubSub {



    public static void main(String[] args) {

        JedisCluster.jedisCluster.psubscribe(new KeyExpiredListener(), "__key*__:*");
    }

}


