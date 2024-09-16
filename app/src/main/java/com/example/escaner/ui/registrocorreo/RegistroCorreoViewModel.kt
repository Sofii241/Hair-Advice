package com.example.escaner.ui.registrocorreo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaner.Repositorios.AuthRepository
import com.example.escaner.Util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class RegistroCorreoViewModel @Inject constructor(
//    DOMINIO DEL REPOSITORIO DE ACCESO
    private val authRepository: AuthRepository
) :  ViewModel() {

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> get() = _mensajeError


    val estadoAcceso: LiveData<Resource<Boolean>> get() = _estadoRegistro
    private val _estadoRegistro = MutableLiveData<Resource<Boolean>>()

    private var checkVerificationJob: Job? = null
    private val _correoVerificado = MutableLiveData<Boolean>()
    val correoVerificado: LiveData<Boolean> get() = _correoVerificado

    fun registro(email: String, password: String, registrado: (AuthResult) -> Unit, fallo: (Exception) -> Unit){
        viewModelScope.launch {
            try {
                val result = authRepository.registro(email, password)
                authRepository.enviarLinkVerificacionCorreo()
                registrado(result)
                verificarConfirmacion()
            } catch (e: Exception) {
                fallo(e)
            }
        }
    }

    private fun verificarConfirmacion() {
        checkVerificationJob?.cancel()
        checkVerificationJob = viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            while (user != null && !user.isEmailVerified) {
                delay(5000)
                user.reload().await()
            }
            if (user != null && user.isEmailVerified) {
                _correoVerificado.postValue(true)
                pararVerificarConfirmacion()
            }
        }
    }

    fun pararVerificarConfirmacion() {
        checkVerificationJob?.cancel()
    }


//    COMPROBACIONES
    fun validarCorreo(email: String): Boolean {
        if (email.isEmpty()) {
            _mensajeError.value = "El correo no puede estar vacío"
            return false
        } else if (!email.contains("@")) {
            _mensajeError.value = "El correo no es válido"
            return false
        }
        _mensajeError.value = null
        return true
    }

    fun validarClave(clave: String): Boolean {
        if (clave.length < 6) {
            _mensajeError.value = "La clave debe tener al menos 6 caracteres"
            return false
        }
        else if (!clave.contains(Regex("[A-Z]"))) {
            _mensajeError.value = "La clave debe tener al menos una mayuscula"
            return false
        }
        else if (!clave.contains(Regex("[^A-Za-z0-9]"))) {
            _mensajeError.value = "La clave debe tener al menos un simbolo"
            return false
        }
        else if (clave.isEmpty()) {
            _mensajeError.value = "La clave no puede estar vacia"
            return false
        }
        _mensajeError.value = null
        return true
    }

    fun validarConfirmacionClave(clave: String, confirmClave: String): Boolean {
        if (clave != confirmClave) {
            _mensajeError.value = "Las claves no coinciden"
            return false
        } else if (confirmClave.isEmpty()) {
            _mensajeError.value = "La confirmación de la clave es obligatoria"
            return false
        }

        _mensajeError.value = null
        return true
    }
}