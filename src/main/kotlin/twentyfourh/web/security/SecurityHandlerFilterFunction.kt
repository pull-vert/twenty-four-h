package twentyfourh.web.security

/**
 * Created by Fred on 13/05/2017.
 */
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod.*
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

/**
 * Created by Fred on 07/11/2016.
 */
class SecurityHandlerFilterFunction(private val jwtUtil: JwtUtil) : HandlerFilterFunction<ServerResponse, ServerResponse> {

    override fun filter(request: ServerRequest, nextFun: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        println("Before handler invocation: " + request.path())

        // N'envoyer sur la suite que si utilisateur habilité ou s'il va sur l'authentification ou creation user
        if ("/login" == request.path() && POST == request.method()
                || "/users" == request.path() && PUT == request.method()
                || OPTIONS == request.method()) {
            return nextFun.handle(request)
        }
        var authorized = false
        val authorizations = request.headers().header(AUTHORIZATION)
        var authToken: String? = null
        if (null != authorizations) {
            for (authorization in authorizations) {
                if (authorization.startsWith("Bearer ")) {
                    authToken = authorization.substring(7)
                    break
                }
            }
        }
        if (null != authToken) {
            val user = jwtUtil.parseToken(authToken)
            authorized = null != user
        }
        if (authorized) {
            return nextFun.handle(request)
        } else {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
        }
    }
}
