package com.dgu.prompt.blaybus_backend.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.expiration}") val expiration: Long
) {
    private val invalidatedTokens = mutableSetOf<String>()

    // 사용자 이름 추출
    fun extractUsername(token: String): String? {
        return extractClaim(token) { it.subject }
    }

    // 특정 클레임 추출
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T? {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    // 토큰 검증
    fun isTokenValid(token: String, username: String): Boolean {
        return validateToken(token, username) && !isTokenInvalidated(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = extractClaim(token) { it.expiration }
        return expiration?.before(Date()) ?: true
    }

    // 토큰 생성
    fun generateToken(username: String): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validateToken(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return extractedUsername == username && !isTokenExpired(token)
    }

    fun invalidateToken(token: String): Boolean {
        return invalidatedTokens.add(token) // 토큰을 블랙리스트에 추가
    }

    fun isTokenInvalidated(token: String): Boolean {
        return invalidatedTokens.contains(token) // 토큰이 블랙리스트에 있는지 확인
    }

}