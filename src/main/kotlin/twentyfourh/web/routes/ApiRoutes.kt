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
        (accept(APPLICATION_JSON) and "/api").nest {
            "/message".nest {
                GET("/", messageHandler::findAll)
            }
        }
    }
}