import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun main() {
    runBlocking(Dispatchers.Default) {

//
//        val c: ReceiveChannel<Int> = produce {
//            var n = 0;
//            while (true) {
//                send(n)
//                n++
//                delay(100)
//            }
//        }
//
//        val f = c.receiveAsFlow().shareIn(this, SharingStarted.Eagerly, 10)
//
//
//        f.stateIn()
//
//        launch {
//            while (true) {
//                println(f.replayCache)
//                delay(200)
//            }
//        }

        val f: Flow<Int> = flow<Int> {
            var n = 0;
            while (true) {
                emit(n)
                n++
                delay(100)
            }
        }.stateIn(this, SharingStarted.Eagerly, 0)

        f.conflate()


        delay(300)

        launch {
            f.collect {
                println(it)
                delay(1000)
            }
        }
//
//        launch {
//            seq.collect {
//                println(it)
//            }
//        }
//
//        val fast = produce {
//            while (true) {
//                delay(1000)
//                send("one")
//                delay(1000)
//                send("two")
//                delay(1000)
//            }
//        }
//
//        val slow = produce {
//            while (true) {
//                delay(3000)
//                send("three")
//            }
//        }

//        while (true) {
//            select<Unit> {
//                fast.onReceive {
//                    println(it)
//                }
//                slow.onReceive {
//                    println(it)
//                }
//            }
//        }
    }
}

fun activeWait(seconds: Int) {
    val start = System.currentTimeMillis();
    val end = start + seconds * 1000
    while (System.currentTimeMillis() <= end) {
        // loop
    }
}

fun CoroutineScope.launchMany(n: Int, block: suspend CoroutineScope.() -> Unit): List<Job> {
    return (1..n).map {
        launch {
            block()
        }
    }
}

private fun pbkdf2(password: String): ByteArray {
    val spec = PBEKeySpec(password.toCharArray(), "salt".toByteArray(), 100000, 256)
    val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    return skf.generateSecret(spec).encoded
}