package jzeus.db

import io.ebean.annotation.WhenCreated
import io.ebean.annotation.WhenModified
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Version
import java.time.LocalDateTime

@MappedSuperclass
open class BaseModel {
    @Id
    var id: Long? = null

    @Version
    var version: Long? = null

    @WhenCreated
    var creationTime: LocalDateTime? = null

    @WhenModified
    var updateTime: LocalDateTime? = null


}

data class DataPage<T>(
    val pageNo: Int,
    val pageSize: Int,
    val total: Int,
    val data: List<T>,
    val hasNext: Boolean
)
