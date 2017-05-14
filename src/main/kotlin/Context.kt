import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.beans.factory.getBean
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactory
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
//    registerBean { ReactiveMongoTemplate(SimpleReactiveMongoDatabaseFactory(
//            ConnectionString(it.environment.getProperty("mongo.uri"))))
//    }
//    registerBean { ReactiveMongoRepositoryFactory(it.getBean<ReactiveMongoTemplate>()) }
    registerBean { ReactorNettyServer(hostname, port ?: it.environment.getProperty("server.port").toInt()) }
//
//    registerBean { MarkdownConverter() }

    // Security
    registerBean { JwtUtil(it.environment.getRequiredProperty("jwt.secret"), it.environment.getRequiredProperty("jwt.hours.validity").toInt()) }
    registerBean<SecurityHandlerFilterFunction>()

    registerBean<MessageRepository>()
//    registerBean<UserRepository>()
//    registerBean<EventRepository>()
//    registerBean<TalkRepository>()
//    registerBean<PostRepository>()

    registerBean<MessageHandler>()


    registerBean<ApiRoutes>()
//    registerBean<BlogController>()
//    registerBean<UserController>()
//    registerBean<EventController>()
//    registerBean<TalkController>()
//    registerBean<NewsController>()
//    registerBean<GlobalController>()
//    registerBean<RedirectController>()
}