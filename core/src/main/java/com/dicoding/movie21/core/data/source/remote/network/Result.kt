package com.dicoding.movie21.core.data.source.remote.network

sealed class Result<out R>{
    data class Success<out T>(val   data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}