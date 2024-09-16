package com.example.escaner.Model.Productos

data class Producto(
    val id: String,
    val nombre: String,
    val marca: String,
    val ingredientes: List<String>,
    val uso: List<String>,
    val recomendaciones: List<Recomendacion>,
    val beneficios: List<String>,
    val idImagenProducto: Int
)