package com.kotlinspring.blogservice.blogservice.config

import com.kotlinspring.blogservice.blogservice.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class AppExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleBadRequestException(ex: MethodArgumentNotValidException): Map<String, Map<String,String?>> {
        val errors = mutableMapOf<String, String?>()
        ex.bindingResult.fieldErrors.forEach {
            errors[it.field] = it.defaultMessage
        }
        return mapOf("errors" to errors);
    }

    @ExceptionHandler(Exception::class)
    fun handleDefaultException(ex: Exception): ErrorResponse {
       return ErrorResponse(ex.message)
    }
}