package redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * redis分布式锁
 * 可以用于防止缓存击穿（即一大批的请求同时到达而缓存没有数据，导致请求都直接查询效率有限的DB）
 */
public class LockTest {

    private static final ExecutorService requestThreadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        for (int i = 0; i <100 ; i++) {
            requestThreadPool.submit(()->{
                LockTest lockTest = new LockTest();
                try {
                    String res = lockTest.getData("laolitest");
                    System.out.println("============================"+res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public String  getData(String key) throws Exception {
        String res = getCache(key);
        if(res == null){
            res = getFromDb();
            JedisCluster.jedisCluster.set(key, res);
        }
        return res;
    }

    public String getFromDb() {
        try {
            Thread.sleep(10000);
            System.out.println("很长时间。。。。。。。。。。。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "dbdata";
    }

    public String getCache(String key) throws Exception {
        String resContent = JedisCluster.jedisCluster.get(key);
        if (resContent == null) {
            String result = JedisCluster.jedisCluster.set(key, "Y", "NX", "PX", 20000);
            if (null == result || !result.equals("OK")) {
                for (int counter = 0; counter < 20; counter++) {
                    resContent = JedisCluster.jedisCluster.get(key);
                    if (resContent != null) {
                        return resContent;
                    } else {
                        Thread.sleep(2000);
                    }
                }
                throw new Exception("Redis Lock Exception:Sleep over " + 2000 + "*5000 milliseconds,the key: " + key);
            }
        }
        return resContent;
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
}



