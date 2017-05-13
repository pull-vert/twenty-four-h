package twentyfourh.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by Fred on 13/05/2017.
 */
@Document
data class Message(
        val msg: String,
        @Id val id: String? = null
        )