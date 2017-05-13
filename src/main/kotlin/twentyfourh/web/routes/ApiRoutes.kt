package twentyfourh.web.routes

import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.Routes
import twentyfourh.util.RouterFunctionProvider
import twentyfourh.web.handlers.MessageHandler

/**
 * Created by Fred on 13/05/2017.
 */
class ApiRoutes(val messageHandler: MessageHandler) : RouterFunctionProvider() {

    override val routes: Routes = {
        ("/api/message" and accept(APPLICATION_JSON)).nest {
            GET("/", messageHandler::findAll)
        }
    }
}