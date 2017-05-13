package twentyfourh.web.handlers

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.*
import twentyfourh.repositories.MessageRepository
import twentyfourh.util.json

/**
 * Created by Fred on 13/05/2017.
 */
class MessageHandler(val repository: MessageRepository) {
    fun findAll(req: ServerRequest) = ok().json().body(repository.findAll())
}