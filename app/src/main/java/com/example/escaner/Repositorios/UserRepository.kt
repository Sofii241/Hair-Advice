package com.example.escaner.Repositorios

import com.example.escaner.Model.Usuarios.UserDAO

interface UserRepository {

    suspend fun almacenarDatosUsuario(uid:String, usuario: UserDAO): Boolean
    suspend fun actualizarDatosUsuario(uid:String, campoActualizar: String, nuevoDato: Any): Boolean

    suspend fun eliminarUsuario(iud: String, usuario: UserDAO): Boolean

    suspend fun buscarDocPorUid_User(iud: String): UserDAO

    suspend fun usuarioRegistrado(userId: String): Boolean






}