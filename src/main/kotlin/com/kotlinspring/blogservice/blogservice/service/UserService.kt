package com.kotlinspring.blogservice.blogservice.service

import com.kotlinspring.blogservice.blogservice.dto.UpdateUserDto
import com.kotlinspring.blogservice.blogservice.dto.UserRegisterDto
import com.kotlinspring.blogservice.blogservice.model.User
import com.kotlinspring.blogservice.blogservice.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) {

    fun createUser(registerDto: UserRegisterDto):User{
        val user=User(email = registerDto.email,
                      password = passwordEncoder.encode(registerDto.password))
        return userRepository.save(user)
    }

    fun getCurrentUser():User{
        val userEmail = SecurityContextHolder.getContext().authentication.name
        return userRepository.findByEmail(userEmail)
    }

    fun updateUser(updateUserDto: UpdateUserDto):User{
        var user=getCurrentUser()
        user=user?.apply {
            userName=updateUserDto.userName
            bio=updateUserDto.bio
            image=updateUserDto.image
        }
        return userRepository.save(user)
    }


}