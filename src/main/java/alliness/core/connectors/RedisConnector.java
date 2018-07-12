package alliness.core.connectors;

import redis.clients.jedis.Jedis;

public class RedisConnector {

    private static RedisConnector instance;

    private Jedis redis;

    public static RedisConnector getInstance(String host, int port) {
        if (instance == null) {
            instance = new RedisConnector(host, port);
        }
        return instance;
    }

    private RedisConnector(String host, int port) {
        redis = new Jedis(host, port);
        redis.connect();
    }

    public String get(String key) {
        return redis.get(key);
    }

}
