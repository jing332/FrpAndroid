package com.github.jing332.frpandroid.util

import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class ThrottleUtil(private val scope: CoroutineScope = GlobalScope, val time: Long = 100L) {
    private var job: Job? = null

    fun runAction(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        action: suspend () -> Unit,
    ) {
        job?.cancel()
        job = null
        job = scope.launch(dispatcher) {
            delay(time)
            action.invoke()
        }
    }
}