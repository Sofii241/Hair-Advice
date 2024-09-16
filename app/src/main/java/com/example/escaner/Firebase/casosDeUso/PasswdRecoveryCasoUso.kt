package com.example.escaner.Firebase.casosDeUso

import android.util.Log
import com.example.escaner.Util.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PasswdRecoveryCasoUso @Inject constructor() {

    suspend operator fun invoke(email: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.cargando)
            var isSuccessful = false
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { isSuccessful = it.isSuccessful }.await()
            emit(Resource.completado(isSuccessful))
            emit(Resource.finalizado)
        } catch (e: Exception) {
            emit(Resource.error(e.message.toString()))
            emit(Resource.finalizado)
            Log.d("ERROR PASSWD RECOVERY", e.message.toString())
        }
    }
}