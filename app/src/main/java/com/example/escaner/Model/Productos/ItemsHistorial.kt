package com.example.escaner.Model.Productos

data class ItemsHistorial(
    val id: Int,
    val idProducto: String,
    val nombreProducto: String,
    val fechaEscaneado: String)