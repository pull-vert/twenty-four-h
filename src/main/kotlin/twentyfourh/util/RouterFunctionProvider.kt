package twentyfourh.util

/**
 * Created by Fred on 13/05/2017.
 */
import org.springframework.web.reactive.function.server.*

abstract class RouterFunctionProvider : () -> RouterFunction<ServerResponse> {

    override fun invoke(): RouterFunction<ServerResponse> = RouterFunctionDsl().apply(routes).router()

    abstract val routes: Routes

}