package twentyfourh.web.security

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm.HS512
import io.jsonwebtoken.impl.TextCodec.BASE64
import twentyfourh.model.Member
import twentyfourh.model.Role
import twentyfourh.util.toDate
import java.time.Instant
import java.time.temporal.ChronoUnit.HOURS

/**
 * Created by Fred on 13/05/2017.
 */
class JwtUtil(val secret: String, val validity: Int) {

    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.

     * @param token the JWT token to parse
     *
     * @return the User object extracted from specified token or null if a token is invalid.
     */
    fun parseToken(token: String): Member? {
        try {
            val body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body

            val u = Member(
                    body["userId"] as String,
                    body.subject,
                    Role.valueOf(body["role"] as String))

            return u

        } catch (e: JwtException) {
            return null
        } catch (e: ClassCastException) {
            return null
        }

    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.

     * @param u the user for which the token will be generated
     * *
     * @return the JWT token
     */
    fun generateToken(u: Member): String {
        val claims = Jwts.claims()
        claims.subject = u.pseudo
        claims["userId"] = u.id
        claims["roles"]= u.role.name

        val now = Instant.now()
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now.toDate())
                .setExpiration(now.plus(validity.toLong(), HOURS).toDate())
                .signWith(HS512, BASE64.decode(secret))
                .compact()
    }
}