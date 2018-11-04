
package com.systemDemo.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 

/**
 * 描述：读取redis 配置并且进行初始化
 *
 * @version v1.0
 */

@Configuration
@EnableCaching
public class RedisCacheConf extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password;

	public static JedisPool jedisPool = null;

    @Bean
    public JedisPool redisPoolFactory() {
		
		if(jedisPool == null) {
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			jedisPoolConfig.setMaxIdle(maxIdle);
			jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

			//JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
			jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					jedisPool.destroy();
				}
			});

		}

		return jedisPool;
    }

}
