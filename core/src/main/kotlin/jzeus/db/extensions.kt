package jzeus.db

import io.ebean.DB
import io.ebean.PagedList
import java.util.concurrent.Future

fun <T : BaseModel> T.save(): T {
    DB.save(this)
    return this
}

fun <T : BaseModel> T.update(): T {
    DB.update(this)
    return this
}

fun <T : BaseModel> T.delete(): Boolean {
    return DB.delete(this)
}

fun <T : BaseModel> T.refresh(): T {
    DB.refresh(this)
    return this
}

fun <T, R> PagedList<T>.map(mapper: (T) -> R): PagedList<R> {
    return object : PagedList<R> {
        override fun loadCount() = this@map.loadCount()
        override fun getFutureCount() = this@map.futureCount
        override fun getList() = this@map.list.map(mapper)
        override fun getTotalCount() = this@map.totalCount
        override fun getTotalPageCount() = this@map.totalPageCount
        override fun getPageSize(): Int = this@map.pageSize
        override fun getPageIndex(): Int = this@map.pageIndex
        override fun hasNext(): Boolean = this@map.hasNext()
        override fun hasPrev(): Boolean = this@map.hasPrev()
        override fun getDisplayXtoYofZ(to: String?, of: String?) = this@map.getDisplayXtoYofZ(to, of)
    }
}
