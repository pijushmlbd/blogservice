package com.kotlinspring.blogservice.blogservice.dto

data class UpdateUserDto (
    val userName:String,
    val bio:String?,
    val image:String?,
)