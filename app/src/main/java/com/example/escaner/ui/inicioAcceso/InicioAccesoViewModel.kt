package com.example.escaner.ui.inicioAcceso

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaner.Repositorios.AuthRepository
import com.example.escaner.Repositorios.UserRepository
import com.example.escaner.Util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InicioAccesoViewModel@Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) :  ViewModel() {

    private val _estadoInicio: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val estadoInicio: LiveData<Resource<Boolean>> get() = _estadoInicio

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> get() = _mensajeError

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val resultado = authRepository.acceso(email, password)
            _estadoInicio.value = resultado
            if (resultado is Resource.error) {
                _mensajeError.value = resultado.message
            }
        }
    }


    fun validarCorreo(email: String): Boolean {
        return if (email.isEmpty()) {
            _mensajeError.value = "El correo no puede estar vacio"
            false
        } else if (!email.contains("@")) {
            _mensajeError.value = "El correo introducido no es valido"
            false
        } else {
            _mensajeError.value = null
            true
        }
    }

    fun accesoGoogle(idToken: String, onRegistered: () -> Unit, onNotRegistered: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val result = authRepository.accesoGoogle(idToken)
                val user = result.user
                if (user != null) {
                    val isRegistered = userRepository.usuarioRegistrado(user.uid)
                    if (isRegistered) {
                        onRegistered()
                    } else {
                        onNotRegistered()
                    }
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }


}