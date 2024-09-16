package com.example.escaner.Firebase.user

import android.util.Log
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.Repositorios.UserRepository
import com.example.escaner.Util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImplement @Inject constructor(
    private val provider: FirebaseFirestore
): UserRepository {

    override suspend fun almacenarDatosUsuario(uid: String, usuario: UserDAO): Boolean = withContext(Dispatchers.IO) {
        try {
            provider.collection("usuarios").document(uid).set(usuario).await()
            Resource.completado(true)
            true
        } catch (e: Exception) {
            Log.d("ERROR AGREGAR USER", "${e.message}")
            Resource.completado(false)
            false
        }
    }

    override suspend fun actualizarDatosUsuario(uid:String, campoActualizar: String, nuevoDato: Any): Boolean = withContext(Dispatchers.IO) {
        try {
            provider.collection("usuarios").document(uid).update(campoActualizar, nuevoDato).await()
            Resource.completado(true)
            true
        } catch (e: Exception) {
            Log.d("ERROR ACTUALIZAR USER", "${e.message}")
            Resource.completado(false)
            false
        }
    }

    override suspend fun eliminarUsuario(iud: String, usuario: UserDAO): Boolean = withContext(Dispatchers.IO) {
        try {
            provider.collection("usuarios").document(iud).delete().await()
            Resource.completado(true)
            true
        } catch (e: Exception) {
            Log.d("ERROR ELIMINAR USUARIO", "${e.message}")
            Resource.completado(false)
            false
        }
    }

    override suspend fun buscarDocPorUid_User(iud: String): UserDAO = withContext(Dispatchers.IO) {
        try {
            val document = provider.collection("usuarios").document(iud).get().await()
            if (document.exists()) {
                document.toObject(UserDAO::class.java) ?: UserDAO()
            } else {
                Log.d("USER OBTENIDO REPOSITORIO", "No existe el usuario con UID: $iud")
                UserDAO()
            }
        } catch (e: Exception) {
            Log.d("ERROR AL BUSCAR ID USER", "${e.message}")
            UserDAO()
        }
    }

    override suspend fun usuarioRegistrado(userId: String): Boolean {
        val userDoc = provider.collection("usuarios").document(userId).get().await()
        return userDoc.exists()
    }


}