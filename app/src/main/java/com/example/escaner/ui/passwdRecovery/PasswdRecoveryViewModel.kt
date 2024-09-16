package com.example.escaner.ui.passwdRecovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.escaner.Repositorios.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PasswdRecoveryViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> get() = _mensajeError


    private val _passwordChanged = MutableLiveData<Boolean>()
    val passwordChanged: LiveData<Boolean> get() = _passwordChanged

    private var checkPasswordResetJob: Job? = null

    fun enviarCorreoRecuperacionClave(email: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.enviarRecuperacionClave(email)
                onSuccess()
                checkRecuperacionClave()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    private fun checkRecuperacionClave() {
        checkPasswordResetJob?.cancel()
        checkPasswordResetJob = viewModelScope.launch {
            val initialSignInTimestamp = FirebaseAuth.getInstance().currentUser?.metadata?.lastSignInTimestamp
            while (true) {
                delay(5000) // Verificar cada 5 segundos
                val user = FirebaseAuth.getInstance().currentUser
                user?.reload()?.await() // Recargar información del usuario
                val currentSignInTimestamp = user?.metadata?.lastSignInTimestamp
                // Verificar si el timestamp de último inicio de sesión ha cambiado
                if (currentSignInTimestamp != null && initialSignInTimestamp != null && currentSignInTimestamp > initialSignInTimestamp) {
                    _passwordChanged.postValue(true)
                    stopCheckRecuperacionClave()
                    break
                }
            }
        }
    }

    fun stopCheckRecuperacionClave() {
        checkPasswordResetJob?.cancel()
    }

    fun validarCorreo(email: String): Boolean {
        if (email.isEmpty()) {
            _mensajeError.value = "El correo no puede estar vacio"
            return false
        } else if (!email.contains("@")) {
            _mensajeError.value = "El correo no es valido"
            return false
        }
        _mensajeError.value = null
        return true
    }
}