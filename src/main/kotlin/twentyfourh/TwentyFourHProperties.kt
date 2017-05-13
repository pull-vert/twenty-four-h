package twentyfourh

//import org.springframework.boot.context.properties.ConfigurationProperties

//@ConfigurationProperties("24h")
class TwentyFourHProperties {
    var baseUri: String? = null
    val admin = Credential()

    class Credential {
        var username: String? = null
        var password: String? = null
    }
}


