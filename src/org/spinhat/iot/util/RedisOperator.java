package org.spinhat.iot.util;

import org.spinhat.iot.IotSysdConfig;

import redis.clients.jedis.Jedis;

public class RedisOperator {
	
	private int deviceExpiredTime = 10;
	private Jedis jedis = null;
	private String redisMaster = null;
	private int redisPort;
	
	public RedisOperator()
	{
		this.redisMaster = IotSysdConfig.getRedisMaster();
		this.redisPort = IotSysdConfig.getRedisPort();
		jedis = new Jedis(this.redisMaster, this.redisPort);
	}

	public String getWriteDataByCMD(String name, String cmd)
	{
		String key = name + ":" + cmd + ":" + "data";
		return jedis.get(key);
	}
	
	public void delWriteDataByCMD(String name, String cmd)
	{
		String key = name + ":" + cmd + ":" + "data";
		jedis.del(key);
	}
	
	public void updateWriteStatusByCMD(String name, String cmd, String status)
	{
		String key = name + ":" + cmd + ":" + "status";
		jedis.set(key, status);
	}
	
	public void updateDeviceData(String name, String cmd, String data)
	{
		String key = name + ":" + cmd + ":" + "data";
		jedis.set(key, data);
		this.updateDeviceAliveStatus(name);
	}
	
	public void updateDeviceAliveStatus(String name)
	{
		String key = name + ":" + "alive";
		jedis.set(key, "alive");
		jedis.expire(key, this.deviceExpiredTime);
	}
	
	public boolean isDeviceAlive(String name)
	{
		String key = name + ":" + "alive";	
		if (jedis.get(key) != null)
		{
			return true;
		}
		return false;
	}
	
	public void clearAliveStatus(String name)
	{
		String key = name + ":" + "alive";
		jedis.del(key);
	}
}
