package com.kotlinspring.blogservice.blogservice.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtTokenUtil {

    //TODO keep key in file
    private val secret_key="secret"
    private val JWT_TOKEN_VALIDITY=60000

    fun getUsernameFromToken(token: String):String?=getClaimFromToken(token) { it!!.subject }

    fun getExpirationDateFromToken(token: String)=getClaimFromToken(token){it!!.expiration}

    fun <T> getClaimFromToken(token: String, claimsResolver: java.util.function.Function<Claims?, T>):T{
        val claims:Claims=getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    fun getAllClaimsFromToken(token: String):Claims{
        return Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).body
    }

    fun isTokenExpired(token: String):Boolean=getExpirationDateFromToken(token).before(Date())

    fun generateToken(userDetails: UserDetails):String{
        val claims=HashMap<String,Any>()
        return doGenerateToken(claims,userDetails.username)

    }

    fun doGenerateToken(claims: Map<String,Any>,subject:String):String{
        return Jwts.builder().setClaims(claims).setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS512, secret_key).compact()
    }

    fun validToken(token: String,userDetails: UserDetails?):Boolean{
        val username=getUsernameFromToken(token)
        return username==userDetails?.username&& !isTokenExpired(token)
    }
}