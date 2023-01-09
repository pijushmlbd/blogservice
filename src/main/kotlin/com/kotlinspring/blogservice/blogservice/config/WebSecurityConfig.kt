package com.kotlinspring.blogservice.blogservice.config

import com.kotlinspring.blogservice.blogservice.filters.JwtAuthenticationFilter
import com.kotlinspring.blogservice.blogservice.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.util.*


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    val userDetailsService: UserDetailsService,
    val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun authenticationManager(httpSecurity: HttpSecurity): AuthenticationManager {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {

       return httpSecurity.csrf().disable()
            .authorizeHttpRequests()
            .antMatchers(HttpMethod.POST,"/users","/users/login")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .authenticationManager(authenticationManager(httpSecurity))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }


}

@Component
class CustomUserDetailsService(
    val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("email not found")
        return SystemUserDetails(
            email,
            user.password,
            Collections.singleton(SimpleGrantedAuthority("user"))
        )
    }
}

class SystemUserDetails(
    val email: String,
    private val uPassword: String,
    private val uAuthorities: MutableCollection<GrantedAuthority>
) : UserDetails {
    override fun getAuthorities() = uAuthorities

    override fun getPassword() = uPassword

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}