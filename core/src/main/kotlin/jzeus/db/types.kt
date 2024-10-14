package jzeus.db


data class DataPage<T>(
    val pageNo: Int,
    val pageSize: Int,
    val total: Int,
    val data: List<T>,
    val hasNext: Boolean
)
