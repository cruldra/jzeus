package jzeus.bool

import java.util.function.Supplier


class Predicate private constructor(private val test: Supplier<Boolean>) {


    // 2. and(otherPredicate)
    fun and(other: Predicate): Predicate {
        return Predicate { test.get() && other.test.get() }
    }

    // 用于测试的方法
    fun test(): Boolean {
        return test.get()
    }

    companion object {

        // 1. of(()->Boolean)
        fun of(test: Supplier<Boolean>): Predicate {
            return Predicate(test)
        }
    }
}

fun main() {




    // 创建一些测试谓词
    val p1 = Predicate.of { true }
    val p2 = Predicate.of { 5 > 3 }
    val p3 = Predicate.of { "hello".length == 5 }


    // 测试单个谓词
    println("p1: " + p1.test()) // true
    println("p2: " + p2.test()) // true
    println("p3: " + p3.test()) // true


    // 测试 and 方法
    val p4 = p1.and(p2).and(p3)
    println("p4 (p1 and p2 and p3): " + p4.test()) // true





    // 添加一个返回 false 的谓词
    val p5 = Predicate.of { false }
    println("p5: " + p5.test()) // false

}
