package redis;

import redis.clients.jedis.*;

public class PubSubTest {



    public static void main(String[] args) {

//        config(jedis);//建议在redis配置文件中设置
        JedisCluster.jedisCluster.set("notify", "新浪微博：小叶子一点也不逗");
        JedisCluster.jedisCluster.expire("notify", 10);
    }

}

class KeyExpiredListener extends JedisPubSub {

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPSubscribe "
                + pattern + " " + subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

        System.out.println("onPMessage pattern "
                + pattern + " " + channel + " " + message);
    }


}

