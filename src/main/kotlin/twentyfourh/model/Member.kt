package twentyfourh.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import twentyfourh.model.Role.USER
import java.util.*

/**
 * Created by Fred on 13/05/2017.
 */
@Document
data class Member(
        @Id val id: String,
        val pseudo: String,
        val role: Role = USER,
        val eMail: String? = null,
        val pass: String? = null,
        val avatarUrl: String? = null,
        val rank: Int? = null,
        val lastVisit: Date? = null,
        val registerDate: Date? = null
)

enum class Role {
    STAFF,
    USER
}