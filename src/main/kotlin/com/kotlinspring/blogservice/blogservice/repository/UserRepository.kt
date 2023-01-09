package com.kotlinspring.blogservice.blogservice.repository

import com.kotlinspring.blogservice.blogservice.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository:JpaRepository<User,Long> {
       fun findByEmail(email:String):User
}