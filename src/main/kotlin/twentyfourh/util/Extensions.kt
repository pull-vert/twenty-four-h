package twentyfourh.util

import com.mongodb.client.result.DeleteResult
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.ResourcePropertySource
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.MediaType.*
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import twentyfourh.model.Language
import java.net.URI
import java.nio.charset.StandardCharsets
import java.text.Normalizer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.reflect.KClass

// ----------------------
// Spring Data extensions
// ----------------------

inline fun <reified T : Any> ReactiveMongoOperations.findById(id: Any): Mono<T> =
        findById(id, T::class.java)

inline fun <reified T : Any> ReactiveMongoOperations.find(query: Query): Flux<T> =
        find(query, T::class.java)

inline fun <reified T : Any> ReactiveMongoOperations.findAll(): Flux<T> =
        findAll(T::class.java)

inline fun <reified T : Any> ReactiveMongoOperations.findOne(query: Query): Mono<T> =
        find(query, T::class.java).next()

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any> ReactiveMongoOperations.remove(query: Query): Mono<DeleteResult> =
        remove(query, T::class.java)

inline fun <reified T : Any> ReactiveMongoOperations.count(): Mono<Long> =
        count(Query(), T::class.java)

// -------------------------
// Spring core extensions
// -------------------------

fun ConfigurableEnvironment.addPropertySource(location: String) {
    propertySources.addFirst(ResourcePropertySource(EncodedResource(ClassPathResource(location), StandardCharsets.UTF_8)))
}

// -------------------------
// Spring WebFlux extensions
// -------------------------

fun ServerRequest.language() =
        Language.findByTag(this.headers().asHttpHeaders().acceptLanguageAsLocales.first().language)

fun ServerRequest.locale() =
        this.headers().asHttpHeaders().acceptLanguageAsLocales.first() ?: Locale.ENGLISH

fun ServerResponse.BodyBuilder.json() = contentType(APPLICATION_JSON_UTF8)

//fun ServerResponse.BodyBuilder.xml() = contentType(APPLICATION_XML)
//
//fun ServerResponse.BodyBuilder.html() = contentType(TEXT_HTML)

fun permanentRedirect(uri: String) = permanentRedirect(URI(uri)).build()

fun seeOther(uri: String) = seeOther(URI(uri)).build()

// --------------------
// Date/Time extensions
// --------------------

fun LocalDateTime.formatDate(language: Language): String =
        if (language == Language.ENGLISH) this.format(englishDateFormatter) else this.format(frenchDateFormatter)

fun LocalDateTime.formatTalkDate(language: Language): String =
        if (language == Language.ENGLISH) this.format(englishTalkDateFormatter) else this.format(frenchTalkDateFormatter).capitalize()

fun LocalDateTime.formatTalkTime(language: Language): String =
        if (language == Language.ENGLISH) this.format(englishTalkTimeFormatter) else this.format(frenchTalkTimeFormatter)

fun LocalDateTime.toRFC3339(): String = ZonedDateTime.of(this, ZoneOffset.UTC) .format(rfc3339Formatter)

fun Instant.toDate(): Date = Date.from(this)

private val daysLookup: Map<Long, String> =
        IntStream.rangeClosed(1, 31).boxed().collect(Collectors.toMap(Int::toLong, ::getOrdinal))

private val frenchDateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH)

private val englishDateFormatter = DateTimeFormatterBuilder()
        .appendPattern("MMMM")
        .appendLiteral(" ")
        .appendText(ChronoField.DAY_OF_MONTH, daysLookup)
        .appendLiteral(" ")
        .appendPattern("yyyy")
        .toFormatter(Locale.ENGLISH)

private val frenchTalkDateFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.FRENCH)

private val frenchTalkTimeFormatter = DateTimeFormatter.ofPattern("HH'h'mm", Locale.FRENCH)

private val englishTalkDateFormatter = DateTimeFormatterBuilder()
        .appendPattern("EEEE")
        .appendLiteral(" ")
        .appendPattern("MMMM")
        .appendLiteral(" ")
        .appendText(ChronoField.DAY_OF_MONTH, daysLookup)
        .toFormatter(Locale.ENGLISH)

private val englishTalkTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)

private val rfc3339Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")



private fun getOrdinal(n: Int) =
        when {
            n in 11..13 -> "${n}th"
            n % 10 == 1 -> "${n}st"
            n % 10 == 2 -> "${n}nd"
            n % 10 == 3 -> "${n}rd"
            else -> "${n}th"
        }

// ----------------
// Other extensions
// ----------------

fun String.stripAccents() = Normalizer
        .normalize(this, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

fun String.toSlug() = toLowerCase()
        .stripAccents()
        .replace("\n", " ")
        .replace("[^a-z\\d\\s]".toRegex(), " ")
        .split(" ")
        .joinToString("-")
        .replace("-+".toRegex(), "-")   // Avoid multiple consecutive "--"

fun <T> Iterable<T>.shuffle(): Iterable<T> =
        toMutableList().apply { Collections.shuffle(this) }

fun localePrefix(locale: Locale) = if (locale.language == "en") "/en" else ""
