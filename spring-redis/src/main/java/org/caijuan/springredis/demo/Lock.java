package org.caijuan.springredis.demo;

public interface Lock {
    
	/**
	 * 	单次锁处理
	 *	
	 * @param key 锁key
	 * @param callBack 业务处理
	 * @param expereTime 超时时长（秒）
	 */
    Object singleLockHandle(String key, LockCallBack callBack, long expereTime);
    
    /**
     * 	请求锁处理
     *	
     * @param key 锁key
     * @param callBack 业务处理
     * @param expereTime 超时时长（秒）
     * @return
     */
    Object requestLockHandle(String key, LockCallBack callBack , long expereTime);
    
    
}