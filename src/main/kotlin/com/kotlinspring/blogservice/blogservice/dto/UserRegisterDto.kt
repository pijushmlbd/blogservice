package com.kotlinspring.blogservice.blogservice.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserRegisterDto(
    @field :NotBlank(message = "can not be blank")
    val email:String,
    @field: Size(min = 6, message = "password must be of at least 6 characters")
    val password:String
)