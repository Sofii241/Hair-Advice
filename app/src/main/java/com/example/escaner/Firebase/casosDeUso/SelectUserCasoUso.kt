package com.example.escaner.Firebase.casosDeUso

import android.util.Log
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.Repositorios.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SelectUserCasoUso @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(uid: String): Flow<UserDAO> = flow {
        try {
//            emit(Resource.cargando)
            val user = userRepository.buscarDocPorUid_User(uid)
            emit(user)
//            emit(Resource.completado(user))
        } catch (e: Exception) {
//            emit(Resource.error("No se pudo obtener el id del usuario: ${e.message}"))
            Log.d("No se pudo obtener el id del usuario:", " ${e.message}")
        }
    }
}

