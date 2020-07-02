package com.decard.zj.founctiontest.kotlin.coroutine.basic

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//fun main() {
//    GlobalScope.launch {
//        delay(1000)
//        println("World!")
//    }
//    println("Hello")
////    Thread.sleep(2000)
//    runBlocking {
//        delay(2000)
//    }
//}

//fun main() = runBlocking {
//    GlobalScope.launch {
//        delay(1000)
//        println("world")
//    }
//    println("hello")
//    delay(2000)
//}

//fun main() = runBlocking {
//    val job = GlobalScope.launch {
//        delay(1000)
//        println("world")
//    }
//    println("hello")
//    job.join()
//}

//fun main() = runBlocking {
//    launch {
//        delay(1000)
//        println("world")
//    }
//    println("hello")
//}

//fun main() = runBlocking {
//    launch {
//        delay(200)
//        println("task from runBlocking")
//    }
//    //创建一个协程作用域
//    coroutineScope {
//        launch {
//            delay(500)
//            println("task from nested launch")
//        }
//        delay(100)
//        println("task from coroutine scope")
//    }
//    println("coroutine scope is over")
//}

/**
 * 提取函数重构
 */
fun main() = runBlocking {
    launch {
        doWorld()
    }
    println("hello")
}

private suspend fun doWorld() {
    delay(1000)
    println("World")
}