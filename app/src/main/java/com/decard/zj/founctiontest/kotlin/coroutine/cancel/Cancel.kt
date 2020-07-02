package com.decard.zj.founctiontest.kotlin.coroutine.cancel

import kotlinx.coroutines.*

/**
 * 取消协程的执行
 * @author ZJ
 * created at 2020/4/26 15:01
 */
fun main0() = runBlocking {
    val job = launch(Dispatchers.Default) {
        repeat(1000) {
            println("job: I'm  sleeping $it")
            delay(500)
        }
    }
    delay(1600)
    println("main: I'm tired of waiting")
    job.cancel()
    job.join()//等待作业执行结束
    println("main: now i can quit...")

}

/**
 * 取消是协作的
 * @author ZJ
 * created at 2020/4/26 15:02
 */
fun main1() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 10) { // 一个执行计算的循环，只是为了占用 CPU
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1600L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个作业并且等待它结束
    println("main: Now I can quit.")
}

/**
 * 取消计算代码1
 * 显示的检查取消代码
 *  while (i < 5) 替换为 while (isActive)
 * @author ZJ
 * created at 2020/4/26 15:10
 */
fun main2() = runBlocking<Unit> {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // 可以被取消的计算循环
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1600L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并等待它结束
    println("main: Now I can quit.")

}


/**
 * 取消计算代码2
 * 定期调用挂起函数来检查取消
 * @author ZJ
 *
 * created at 2020/4/26 15:14
 */
fun main3() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 10) { // 一个执行计算的循环，只是为了占用 CPU
            yield()
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1400L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个作业并且等待它结束
    println("main: Now I can quit.")
}


/**
 * finally中释放资源
 *
 * @author ZJ
 * created at 2020/4/26 15:21
 */
fun main4() = runBlocking {
    val job = launch {
        try {
            repeat(1000) {
                println("job: I'm sleeping $it")
                delay(500)
            }
        } finally {
            println("job: I'm running finally")
        }
    }
    delay(1600)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并且等待它结束
    println("main: Now I can quit.")
}


/**
 * 运行不能取消的代码块
 * @author ZJ
 * created at 2020/4/26 16:19
 */
fun main5() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该作业并等待它结束
    println("main: Now I can quit.")

}


/**
 * 超时
 * @author ZJ
 * created at 2020/4/26 16:30
 */
fun main() = runBlocking {
    withTimeout(1600) {
        repeat(10000) {
            println("$it")
            delay(400)
        }
    }
}