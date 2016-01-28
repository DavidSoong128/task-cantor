package com.horizon.cantor.redis;

import com.horizon.cantor.config.CantorConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SingleRedisHolder {

	private JedisPool pool;
	
	private SingleRedisHolder(){
		String host = CantorConfig.configHolder().getRedisHost();
		int port = CantorConfig.configHolder().getRedisPort();
		init(host,port);
	}
	private static class Holder{
		private static SingleRedisHolder holder = new SingleRedisHolder();
	}
	public static SingleRedisHolder redisHolder(){
		return Holder.holder;
	}
	
	private void init(String host,int port) {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			// jedis的最大分配对象#  
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(100);
			//#jedis最大保存idel状态对象数 #  
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(50);
			//#jedis池没有对象返回时，最大等待时间 #
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(1000 * 100);
			//#jedis调用borrowObject方法时，是否进行有效检查#
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			//#jedis调用returnObject方法时，是否进行有效检查 #
			//config.setTestOnReturn(true);
			pool = new JedisPool(config, host, port);
			
		}
	}
	
	public Jedis getJedis(){
		return pool.getResource();
	}
	
	public void returnJedis(Jedis jedis){
		pool.returnResource(jedis);
	}
}
