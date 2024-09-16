package com.example.escaner.Util

sealed class Resource <out R> {
    data class completado<out T>(val data: T): Resource<T>()
    data class error(val message: String): Resource<Nothing>()

    data object cargando : Resource<Nothing>()
    data object finalizado: Resource<Nothing>()
}