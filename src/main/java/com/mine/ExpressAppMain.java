package com.mine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootApplication
@EnableRedisRepositories
public class ExpressAppMain {
	
	@Value("${redis.hostname}")
	String redisHostName;
	
	@Value("${redis.port}")
	int redisPort;
	
	@Value("${redis.password}")
	String redisPassword;
	
	@Autowired
	JedisPoolConfig jedisPoolConfig;
	
	@Bean
	public JedisPoolConfig getJedisPoolConfig(){
		JedisPoolConfig jedisPoolConfig= new JedisPoolConfig();
		jedisPoolConfig.setMinIdle(1);
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxTotal(30);
		jedisPoolConfig.setMaxWaitMillis(1000);
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setTestOnReturn(true);
		return jedisPoolConfig;
	}
	
//	@Bean
//	public <K, V> RedisTemplate<K,V>   getRedisTemplate(){
//		
//		JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory();
//		jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
//		jedisConnectionFactory.setUsePool(true);
//		jedisConnectionFactory.setHostName(redisHostName);
//		jedisConnectionFactory.setPort(redisPort);
//		jedisConnectionFactory.setPassword(redisPassword);
//		jedisConnectionFactory.setTimeout(15000);
//		RedisTemplate<K,V> redisTemplate=new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(jedisConnectionFactory);
//		return redisTemplate;
//	}
	
	
	@Bean
	@SuppressWarnings("resource")
	public Jedis getJedisClient(){
		JedisPool pool=new JedisPool(jedisPoolConfig,redisHostName,redisPort,15000,redisPassword);
		return pool.getResource();
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(ExpressAppMain.class, args);
	}

}
