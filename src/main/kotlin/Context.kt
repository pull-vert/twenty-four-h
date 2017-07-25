import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.registerBean
import twentyfourh.repositories.MessageRepository
import twentyfourh.util.ReactorNettyServer
import twentyfourh.util.addPropertySource
import twentyfourh.web.handlers.MessageHandler
import twentyfourh.web.routes.ApiRoutes
import twentyfourh.web.security.JwtUtil
import twentyfourh.web.security.SecurityHandlerFilterFunction

/**
 * Created by Fred on 12/05/2017.
 */
fun context(port: Int?, hostname: String) = AnnotationConfigApplicationContext {
    environment.addPropertySource("application.properties")

    // MongoDB
//    registerBean { ReactiveMongoTemplate(SimpleReactiveMongoDatabaseFactory(
//            ConnectionString(it.environment.getProperty("mongo.uri"))))
//    }
//    registerBean { ReactiveMongoRepositoryFactory(it.getBean<ReactiveMongoTemplate>()) }

    // Reactor Netty Server
    registerBean { ReactorNettyServer(hostname, port ?: it.environment.getProperty("server.port").toInt()) }

    // Security
    registerBean { JwtUtil(it.environment.getRequiredProperty("jwt.secret"), it.environment.getRequiredProperty("jwt.hours.validity").toInt()) }
    registerBean<SecurityHandlerFilterFunction>()

    // Repositories
    registerBean<MessageRepository>()

    // Web handlers
    registerBean<MessageHandler>()

    // Web routes
    registerBean<ApiRoutes>()
}