package com.gj.frameworks.shiro.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisSessionDao extends AbstractSessionDAO {
	
	private String keyPrefix = "shiro_redis_session:";

	// Session超时时间，单位为秒
	private long expireTime = 600000;

	@Lazy
    @Resource
	private RedisTemplate redisTemplate;// Redis操作类，对这个使用不熟悉的，可以参考前面的博客

	public RedisSessionDao() {
		super();
	}

	public RedisSessionDao(long expireTime, RedisTemplate redisTemplate) {
		super();
		this.expireTime = expireTime;
		this.redisTemplate = redisTemplate;
	}

	@Override // 更新session
	public void update(Session session) throws UnknownSessionException {
		System.out.println("===============update================");
		if (session == null || session.getId() == null) {
			return;
		}
		session.setTimeout(expireTime);
		redisTemplate.opsForValue().set(this.keyPrefix+session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
	}

	@Override // 删除session
	public void delete(Session session) {
		System.out.println("===============delete================");
		if (null == session) {
			return;
		}
		redisTemplate.opsForValue().getOperations().delete(this.keyPrefix+session.getId());
	}

	@Override // 获取活跃的session，可以用来统计在线人数，如果要实现这个功能，可以在将session加入redis时指定一个session前缀，统计的时候则使用keys("session-prefix*")的方式来模糊查找redis中所有的session集合
	public Collection<Session> getActiveSessions() {
		System.out.println("==============getActiveSessions=================");
		return redisTemplate.keys(this.keyPrefix+"*");
	}

	@Override // 加入session
	protected Serializable doCreate(Session session) {
		System.out.println("===============doCreate================");
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, this.keyPrefix+sessionId);

		redisTemplate.opsForValue().set(this.keyPrefix+session.getId(), session, expireTime, TimeUnit.SECONDS);
		return sessionId;
	}

	@Override // 读取session
	protected Session doReadSession(Serializable sessionId) {
		System.out.println("==============doReadSession=================");
		if (sessionId == null) {
			return null;
		}
		return (Session) redisTemplate.opsForValue().get(this.keyPrefix+sessionId);
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
