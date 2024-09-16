package com.example.escaner.Firebase.casosDeUso

import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.Util.Resource
import com.example.escaner.Repositorios.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

//EL CASO DE USO ES EL QUE SE LLAMA EN EL VIEWMODEL
//INVOCA LOS METODOS DE LA IMPLEMENTACION DEL REPOSITORIO
class CreateUserCasoUso @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(uid: String, usuario: UserDAO): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.cargando)
            val result = userRepository.almacenarDatosUsuario(uid, usuario)
            emit(Resource.completado(result))
        } catch (e: Exception) {
            emit(Resource.error("No se pudo registrar al usuario: ${e.message}"))
        }
    }
}

