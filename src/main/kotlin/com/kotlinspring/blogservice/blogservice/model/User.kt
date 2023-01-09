package com.kotlinspring.blogservice.blogservice.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.kotlinspring.blogservice.blogservice.dto.CurrentUserDto
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="users")
class User(

    var email:String,
    var token:String="",
    @JsonProperty(value = "username")
    var userName:String="",
    var bio:String?="",
    var image:String?=null,
    var password:String,
    @Id @GeneratedValue var id:Long?=null,

)

fun User.toCurrentUser()=CurrentUserDto(
    email=email,
    userName=userName,
    bio=bio,
    image=image
)