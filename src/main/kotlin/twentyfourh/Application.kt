package twentyfourh

import context
import org.springframework.beans.factory.getBean
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import twentyfourh.util.ReactorNettyServer
import java.util.concurrent.CompletableFuture

class Application {

    val hostname: String
    val port: Int?
    val context: AnnotationConfigApplicationContext

    constructor(port: Int? = null, hostname: String = "0.0.0.0") {
        this.hostname = hostname
        this.port = port
        this.context = context(this.port, this.hostname)
        context.refresh()
    }

    fun start() {
//        context.getBean<UserRepository>().initData()
//        context.getBean<EventRepository>().initData()
//        context.getBean<TalkRepository>().initData()
//        context.getBean<PostRepository>().initData()
        context.getBean<ReactorNettyServer>().start()
    }

    fun await() {
        val stop = CompletableFuture<Void>()
        Runtime.getRuntime().addShutdownHook(Thread {
            stop()
            stop.complete(null)
        })
        stop.get()
    }

    fun stop() {
        context.getBean<ReactorNettyServer>().stop()
    }

}

fun main(args: Array<String>) {
    val application = Application()
    application.start()
    application.await()
}