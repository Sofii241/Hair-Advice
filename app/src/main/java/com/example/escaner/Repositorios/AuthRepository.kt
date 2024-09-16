package com.example.escaner.Repositorios

import com.example.escaner.Util.Resource
import com.google.firebase.auth.AuthResult

interface AuthRepository {

    suspend fun acceso(correo: String, clave: String): Resource<Boolean>

    suspend fun registro(email: String, password: String): AuthResult

    suspend fun accesoGoogle(idToken: String): AuthResult
    suspend fun enviarLinkVerificacionCorreo()

    suspend fun enviarRecuperacionClave(email: String)

}