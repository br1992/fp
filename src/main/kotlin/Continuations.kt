import java.lang.IllegalStateException
import kotlin.coroutines.*

object Continuations {

    fun <Out> generator(genFunc: suspend Generator<Out>.() -> Unit): suspend () -> GeneratorResult<Out> {
        var resume: Continuation<Unit>? = null
        var send: Continuation<GeneratorResult<Out>> = Continuation(EmptyCoroutineContext) {}

        val generator = object: Generator<Out> {
            override suspend fun yield(t: Out) {
                println("Yield")
                suspendCoroutine {
                    resume = it
                    send.resume(GeneratedResult(t))
                }
            }
        }

        return suspend {
            println("Execute")

            suspendCoroutine {
                send = it
                if (resume != null) {
                    resume!!.resume(Unit)
                } else {
                    suspend { genFunc(generator) }.startCoroutine(Continuation(EmptyCoroutineContext) {
                        resume = Continuation(EmptyCoroutineContext) { throw IllegalStateException("Already finished consuming generator") }
                        send.resume(Empty)
                    })
                }
            }
        }
    }

    interface Generator<T> {
        suspend fun yield(t: T)
    }

    interface GeneratorResult<out T>

    object Empty: GeneratorResult<Nothing>
    data class GeneratedResult<T>(val value: T): GeneratorResult<T>

    class ContFunc<T, R>(val block: suspend ContFuncScope<T, R>.(T) -> R) {

        fun execute(t: T): R {
            val scope = object : ContFuncScope<T, R> {

                var result: R? = null
                lateinit var nextCont: Continuation<Any>
                var nextValue: Any = Unit

                override fun execute(value: T): R {
                    nextCont = suspend {
                        block(this, t)
                    }.createCoroutine(Continuation(EmptyCoroutineContext) {
                        result = it.getOrThrow()
                    }) as Continuation<Any>

                    while (true) {
                        nextCont.resume(nextValue)

                        if (result != null) {
                            return result as R
                        } else {
                            continue
                        }
                    }
                }

                override suspend fun callWithCont(value: T): R {
                    return suspendCoroutine { cont ->
                        val resultCont = Continuation<R>(EmptyCoroutineContext) { result ->
                            nextCont = cont as Continuation<Any>
                            nextValue = result.getOrThrow() as Any
                        }
                        nextValue = value as Any
                        nextCont = Continuation(EmptyCoroutineContext) {
                            suspend { block(this, it.getOrThrow() as T) }.startCoroutine(resultCont)
                        }
                    }
                }
            }

            return scope.execute(t)
        }

//        fun execute(t: T): R {
//            var result: R? = null
//            var returnCont = EmptyContinuation
//            var nextCont: Continuation<Unit>
//
//            val scope = object : ContFuncScope<T, R> {
//                override suspend fun callWithCont(value: T): R {
//                    return suspendCoroutine<R> { cont ->
//                        val resultCont = Continuation<R>(EmptyCoroutineContext) { result ->
//                            nextCont = Continuation<Unit>(EmptyCoroutineContext) {
//                                cont.resumeWith(result)
//                            }
//                            returnCont.resume(Unit)
//                        }
//                        nextCont = suspend { block(this, value) }.createCoroutine(resultCont)
//
//                        returnCont.resume(Unit)
//                    }
//                }
//            }
//
//            nextCont = suspend {
//                block(scope, t)
//            }.createCoroutine(Continuation<R>(EmptyCoroutineContext) {
//                result = it.getOrThrow()
//            })
//
//            while (true) {
//                nextCont.resume(Unit)
//
//                if (result != null) {
//                    return result as R
//                } else {
//                    continue
//                }
//            }
//        }

        val EmptyContinuation: Continuation<Unit> = object : Continuation<Unit> {
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Unit>) {}

        }

//        fun execute(t: T): R {
//            var result: R? = null
//            var returnCont = Continuation<Any>(EmptyCoroutineContext) {}
//
//            val scope = object : ContFuncScope<T, R> {
//                override suspend fun callWithCont(value: T): R {
//                    return suspendCoroutine<R> { cont ->
//                        val resultCont = Continuation<R>(EmptyCoroutineContext) { result ->
//                            returnCont.resume(Continuation<R>(EmptyCoroutineContext) {
//                                cont.resume(result.getOrThrow())
//                            })
//                        }
//                        val newCont = suspend { block(this, value) }.createCoroutine(resultCont)
//
//                        returnCont.resume(newCont)
//                    }
//                }
//            }
//
//            var nextCont = suspend {
//                block(scope, t)
//            }.createCoroutine(Continuation<R>(EmptyCoroutineContext) {
//                result = it.getOrThrow()
//            })
//
//            while (true) {
//                returnCont = Continuation(EmptyCoroutineContext) {
//                    nextCont = it.getOrThrow()
//                } as Continuation<Any>
//                nextCont.resume(Unit)
//
//                if (result != null) {
//                    return result as R
//                } else {
//                    continue
//                }
//            }
//        }

//        suspend fun execute(t: T): R {
//            return suspendCoroutine<R> { topCont ->
//                var returnCont = Continuation<Any>(EmptyCoroutineContext) {}
//
//                val scope = object : ContFuncScope<T, R>, Continuation<T> {
//                    override suspend fun callWithCont(value: T): R {
//                        return suspendCoroutine<R> { cont ->
//                            val newCont = Continuation<Unit>(EmptyCoroutineContext) {
//                                suspend { block(this, value) }.startCoroutine(cont)
//                            }
//
//                            returnCont.resume(newCont)
//                        }
//                    }
//                }
//
//                var nextCont = suspend {
//                    block(scope, t)
//                }.createCoroutine(topCont)
//
//                while (true) {
//                    suspend {
//                        suspendCoroutine<> {  }
//                        returnCont = cont
//                        nextCont.resume(Unit)
//                    }.startCoroutine()
////                    if (result != null) {
////                        return result
////                    } else if (nextCont != null) {
////                        nextCont.resume(Unit)
////                    } else {
////                        throw IllegalStateException("Booo!")
////                    }
//                }
//            }

//            return suspendCoroutine<R> { cont ->
//                var scopeNode = ScopeNode(null, cont)
//                val funcScope = object : ContFuncScope<T, R> {
//                    override suspend fun callWithCont(value: T): R {
//                        suspend {
//                            block.invoke(this, value)
//                        }.createCoroutine()
//                        val newScopeNode = ScopeNode<T>(scopeNode, )
//                    }
//                }
//                suspend {
//                    block.invoke(funcScope, t)
//                }.startCoroutine(cont)
//            }
//        }
    }

    data class ScopeNode<T>(val previous: ScopeNode<T>?, val cont: Continuation<T>)

//    internal class ContFuncScopeImpl<T, R>(val func: ContFunc<T, R>): ContFuncScope<T, R> {
//        override suspend fun callWithCont(value: T): R {
//            return suspendCoroutine<R> { cont ->
//                val funcScope = ContFuncScopeImpl<T, R>(func)
//                suspend {
//                    func.block.invoke(funcScope, value)
//                }.startCoroutine(cont)
//            }
//        }
//
//    }

    interface ContFuncScope<T, R> {
        suspend fun callWithCont(value: T): R
        fun execute(value: T): R
    }
}

