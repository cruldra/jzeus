package jzeus.lock

import io.ebean.Database
import jzeus.datetime.Timeout
import jzeus.datetime.plusTimeout
import java.time.LocalDateTime

/**
 * 这个接口定义了数据库锁的获取和释放操作
 * @author dongjak
 * @created 2024/10/09
 * @version 1.0
 * @since 1.0
 */
interface DatabaseLock {


    /**
     * 获取数据库锁
     * @param lockKey 锁的键
     * @param timeout 超时时间
     * @return 是否获取到锁
     */
    fun acquireLock(lockKey: String, timeout: Timeout): Boolean


    /**
     * 释放数据库锁
     * @param lockKey 锁的键
     */
    fun releaseLock(lockKey: String)

    fun releaseAll()
}

class EBeanH2Lock(private val database: Database) : DatabaseLock {
    init {
        database
            .sqlUpdate("CREATE TABLE IF NOT EXISTS locks (lock_key VARCHAR(255) PRIMARY KEY, locked_at TIMESTAMP, expires_at TIMESTAMP)")
            .execute()
    }

    override fun acquireLock(lockKey: String, timeout: Timeout): Boolean {
        cleanExpiredLocks()

        val now = LocalDateTime.now()
        val expirationTime = now.plusTimeout(timeout)

        val inserted = database.sqlUpdate(
            """
            INSERT INTO locks (lock_key, locked_at, expires_at)
            SELECT ?, ?, ?
            WHERE NOT EXISTS (SELECT 1 FROM locks WHERE lock_key = ?)
        """
        ).setParameter(1, lockKey)
            .setParameter(2, now)
            .setParameter(3, expirationTime)
            .setParameter(4, lockKey)
            .execute()

        return inserted > 0
    }

    override fun releaseLock(lockKey: String) {
        database.sqlUpdate("DELETE FROM locks WHERE lock_key = ?")
            .setParameter(1, lockKey).execute()
    }

    override fun releaseAll() {
        database.sqlUpdate("DELETE FROM locks").execute()
    }

    private fun cleanExpiredLocks() {
        database.sqlUpdate("DELETE FROM locks WHERE expires_at < ?")
            .setParameter(1, LocalDateTime.now())
            .execute()
    }
}
