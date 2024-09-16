package com.example.escaner.Model.Usuarios

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import com.example.escaner.Model.Usuarios.Enums.Condicion
import com.example.escaner.Model.Usuarios.Enums.Genero
import com.example.escaner.Model.Usuarios.Enums.Largo
import com.example.escaner.Model.Usuarios.Enums.Tenido
import com.example.escaner.Model.Usuarios.Enums.Textura
import com.example.escaner.Model.Usuarios.Enums.Tipo
import com.example.escaner.Model.Usuarios.Enums.Tratamiento
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class UserDAO(
    var id: String = "",

    var nombre: String = "",
    var apellido: String = "",
    var provincia: String = "",
    var peloColor: String = "",
    var localidad: String = "",
    var alergias: String = "No",

    var fotoPerfil: Boolean = false,

    var fechaNacimiento: Date = Date(),
    var genero: Genero = Genero.Indefinido,

    var peloTenido: Boolean = false,
    var peloTratamiento: Boolean = false,

    var largo: Largo = Largo.Default,
    var peloTipo: Tipo = Tipo.Default,
    var tenido: Tenido = Tenido.Natural,
    var peloTextura: Textura = Textura.Default,
    var peloCondicion: Condicion = Condicion.Default,
    var tipoTratamiento: Tratamiento = Tratamiento.Default,
): Parcelable


