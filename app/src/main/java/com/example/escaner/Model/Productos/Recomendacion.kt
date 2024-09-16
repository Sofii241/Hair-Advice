package com.example.escaner.Model.Productos

import com.example.escaner.Model.Usuarios.Enums.Nivel

data class Recomendacion(
    val id: String,
    val nivel: Nivel,
    val recomendacion: String,
)