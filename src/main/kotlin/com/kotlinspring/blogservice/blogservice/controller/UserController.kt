package com.kotlinspring.blogservice.blogservice.controller

import com.kotlinspring.blogservice.blogservice.dto.*
import com.kotlinspring.blogservice.blogservice.model.User
import com.kotlinspring.blogservice.blogservice.model.toCurrentUser
import com.kotlinspring.blogservice.blogservice.service.UserService
import com.kotlinspring.blogservice.blogservice.util.JwtTokenUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    val jwtTokenUtil: JwtTokenUtil,
    val authenticationManager: AuthenticationManager,
    val userDetailsService: UserDetailsService
) {

    @PostMapping
    fun registerUser(@RequestBody @Valid registerDto: UserRegisterDto): ResponseEntity<User> {
        val createdUser = userService.createUser(registerDto)
        return ResponseEntity(createdUser, HttpStatus.OK)
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginDto: UserLoginDto): ResponseEntity<Any> {

        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(userLoginDto.email, userLoginDto.password)
            )
        } catch (ex: AuthenticationException) {
            throw Exception("Invalid username or password")
        }

        val userDetails = userDetailsService.loadUserByUsername(userLoginDto.email)
        val accessToken = jwtTokenUtil.generateToken(userDetails)

        return ResponseEntity(UserLoginResponse(accessToken), HttpStatus.OK)
    }

    @GetMapping("/current")
    fun getUser(): CurrentUserDto? = userService.getCurrentUser()?.toCurrentUser()

    @PutMapping()
    fun updateUserInfo(@RequestBody updateUserDto: UpdateUserDto):CurrentUserDto=
                           userService.updateUser(updateUserDto)?.toCurrentUser()

}