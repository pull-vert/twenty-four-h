package twentyfourh.repositories

import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import twentyfourh.model.Message

/**
 * Created by Fred on 13/05/2017.
 */
class MessageRepository {
    fun  findAll(): Flux<Message> {
        return listOf(
                Message("msg 1"),
                Message("msg 2")
        ).toFlux()
    }
}