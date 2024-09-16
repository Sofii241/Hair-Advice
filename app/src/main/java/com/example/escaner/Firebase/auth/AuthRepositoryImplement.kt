package com.example.escaner.Firebase.auth

import android.util.Log
import com.example.escaner.Util.Resource
import com.example.escaner.Repositorios.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImplement@Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override suspend fun acceso(correo: String, clave: String): Resource<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(correo, clave).await()
            Resource.completado(true)
        } catch (e: FirebaseAuthException) {
            Log.d("ERROR ACCESO CORREO", "Error: ${e.errorCode} - ${e.message}", e)
            when (e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> Resource.error("El correo electrónico no existe")
                "ERROR_WRONG_PASSWORD" -> Resource.error("La contraseña es incorrecta")
                "ERROR_INVALID_EMAIL" -> Resource.error("Formato de correo electrónico erróneo")
                else -> Resource.error("${e.message}")
            }
        } catch (e: Exception) {
            Log.d("ERROR ACCESO CORREO", e.toString(), e.cause)
            Resource.error(e.message!!)
        }
    }

    override suspend fun registro(correo: String, clave: String): AuthResult {
        return firebaseAuth.createUserWithEmailAndPassword(correo, clave).await()
    }

    override suspend fun accesoGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return firebaseAuth.signInWithCredential(credential).await()
    }

    override suspend fun enviarLinkVerificacionCorreo() {
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()?.await()
    }

    override suspend fun enviarRecuperacionClave(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()

    }


}