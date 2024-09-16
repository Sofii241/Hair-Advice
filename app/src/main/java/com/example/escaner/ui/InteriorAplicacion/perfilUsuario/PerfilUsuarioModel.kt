package com.example.escaner.ui.InteriorAplicacion.perfilUsuario

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaner.Firebase.user.UserRepositoryImplement
import com.example.escaner.Model.Usuarios.UserDAO
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
//INYECTAMOS CONSTRUCTOR DEL CASO DE USO
class  PerfilUsuarioModel @Inject constructor(
    private val userRepository: UserRepositoryImplement,
    firebaseAuth: FirebaseAuth,
): ViewModel() {

    private val _usuarioBuscado: MutableLiveData<UserDAO> = MutableLiveData()
    val usuarioBuscado: LiveData<UserDAO> get() = _usuarioBuscado

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> get() = _mensajeError

    private var usuario: UserDAO = UserDAO()
    private var idCurrentUser = ""

    init {
        idCurrentUser = firebaseAuth.currentUser?.uid.toString()
        selectUser(idCurrentUser)
        Log.d("USER ID EN MODEL", " $idCurrentUser")
    }

    private fun selectUser(id: String) {
        viewModelScope.launch {
            try {
                usuario = userRepository.buscarDocPorUid_User(id)
                _usuarioBuscado.postValue(usuario)
            } catch (e: Exception) {
                Log.d("Error al obtener datos del usuario:", " $usuario")
            }
        }
    }

    fun actualizarUsuario(campoActualizar: String, nuevoDato: Any) {
        viewModelScope.launch {
            try {
                userRepository.actualizarDatosUsuario(idCurrentUser, campoActualizar, nuevoDato)
            } catch (e: Exception) {
                Log.d("Error al actualizar usuario:", " ${e.message}")
            }
        }
    }
}



