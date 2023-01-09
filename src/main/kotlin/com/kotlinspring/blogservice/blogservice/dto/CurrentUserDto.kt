package com.kotlinspring.blogservice.blogservice.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CurrentUserDto(
    val email:String,
    @JsonProperty(value = "username")
    val userName:String,
    val bio:String?,
    val image:String?,
)