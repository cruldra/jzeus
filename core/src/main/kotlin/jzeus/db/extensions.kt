package jzeus.db

import io.ebean.DB

fun <T :  BaseModel> T.save(): T {
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
