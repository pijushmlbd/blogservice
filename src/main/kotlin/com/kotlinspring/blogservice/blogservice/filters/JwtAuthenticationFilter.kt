package com.kotlinspring.blogservice.blogservice.filters

import com.kotlinspring.blogservice.blogservice.util.JwtTokenUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtTokenUtil: JwtTokenUtil
):OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader=request.getHeader("Authorization")

        if(authorizationHeader!=null&& authorizationHeader.startsWith("Bearer ")){
             val token=authorizationHeader.substring(7)
             val email=jwtTokenUtil.getUsernameFromToken(token)
             upDateSecurityContext(token,email)
        }
        filterChain.doFilter(request,response)
    }

    fun upDateSecurityContext(token:String,email:String?){

        val userDetails = email?.let { userDetailsService.loadUserByUsername(email)}

        if(!jwtTokenUtil.validToken(token,userDetails)) return

        val usernamePasswordAuthenticationToken=UsernamePasswordAuthenticationToken(userDetails,null,userDetails?.authorities)
        usernamePasswordAuthenticationToken.details=userDetails
        SecurityContextHolder.getContext().authentication=usernamePasswordAuthenticationToken
    }
}