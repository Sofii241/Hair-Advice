package com.example.escaner.ui.registro.Fragments.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.R
import com.example.escaner.Util.Resource
import com.example.escaner.databinding.CardviewFotoPerfilBinding
import com.example.escaner.ui.MainInteriorAplicacion
import com.example.escaner.ui.registro.RegistroViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditarFotoFragment : Fragment(), View.OnClickListener {
    private val REQUEST_PERMISSION_CAMERA = 100

    companion object {
        fun newInstance() = EditarFotoFragment()

        fun guardarImagenEnAlmacenamientoInterno(context: Context, bitmap: Bitmap, nombreArchivo: String): Uri {
            val archivo = File(context.filesDir, nombreArchivo)
            FileOutputStream(archivo).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }
            return Uri.fromFile(archivo)
        }

        fun guardarImagenEnAlmacenamientoInterno(context: Context, uri: Uri, nombreArchivo: String): Uri {
            val archivo = File(context.filesDir, nombreArchivo)
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(archivo)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            outputStream.close()
            return Uri.fromFile(archivo)
        }

        fun obtenerImagenDesdeAlmacenamientoInterno(context: Context, nombreArchivo: String): Bitmap? {
            val archivo = File(context.filesDir, nombreArchivo)
            return if (archivo.exists()) {
                BitmapFactory.decodeFile(archivo.absolutePath)
            } else {
                null
            }
        }

        suspend fun registrarFotoPerfil(boolean: Boolean) {
            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
            val userId = firebaseAuth.currentUser?.uid.toString()

            actualizarDatosUsuario(userId, "fotoPerfil", boolean)
        }

        private suspend fun actualizarDatosUsuario(uid:String, campoActualizar: String, nuevoDato: Any): Boolean = withContext(Dispatchers.IO) {
            val provider: FirebaseFirestore = FirebaseFirestore.getInstance()
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
    }

    private lateinit var binding: CardviewFotoPerfilBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    private lateinit var bitmap: Bitmap
    private lateinit var imagenGaleria: Uri

    private val startCameraActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            bitmap = result.data?.extras?.get("data") as Bitmap

            binding.imageViewProfile.visibility = View.VISIBLE
            binding.imageViewProfile.setImageBitmap(bitmap)

            lifecycleScope.launch {
                guardarImagenEnAlmacenamientoInterno(requireContext(), bitmap, "FotoPerfilCamara.jpg")

                registrarFotoPerfil(true)
            }
        }
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val data: Intent? = result.data
            imagenGaleria = data?.data!!

            binding.imageViewProfile.visibility = View.VISIBLE
            binding.imageViewProfile.setImageURI(imagenGaleria)

            lifecycleScope.launch {
                guardarImagenEnAlmacenamientoInterno(
                    requireContext(),
                    imagenGaleria,
                    "FotoPerfilGaleria.jpg"
                )

                registrarFotoPerfil(true)
            }
        } else {
            Toast.makeText(requireContext(), "Cancelado por usuario", Toast.LENGTH_SHORT).show()
            Log.d("Foto perfil galeria", "Cancelada por el usuario")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewFotoPerfilBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCamera.setOnClickListener(this)
        binding.buttonGallery.setOnClickListener(this)
        binding.buttonSinFoto.setOnClickListener(this)
        binding.next7.setOnClickListener(this)
    }

    private fun disableAll() {
        binding.buttonCamera.isSelected = false
        binding.buttonGallery.isSelected = false
        binding.buttonSinFoto.isSelected = false
    }

    override fun onClick(v: View) {
        val id = v.id

        if (id == binding.buttonCamera.id) {
            permisosCamara()
            binding.error4.visibility = View.INVISIBLE

            disableAll()

            binding.buttonCamera.isSelected = true
        } else if (id == binding.buttonGallery.id) {
            abrirGaleria()
            binding.error4.visibility = View.INVISIBLE

            disableAll()

            binding.buttonGallery.isSelected = true
        } else if (id == binding.buttonSinFoto.id) {
            val sinFoto = R.mipmap.ic_sin_foto_perfil

            binding.imageViewProfile.visibility = View.VISIBLE
            binding.imageViewProfile.setImageResource(sinFoto)

            disableAll()

            binding.buttonSinFoto.isSelected = true

            lifecycleScope.launch {
                registrarFotoPerfil(false)
            }
        } else if (id == binding.next7.id) {
            viewModel.controlSeleccion(
                binding.buttonCamera,
                binding.buttonGallery,
                binding.buttonSinFoto
            )

            if (!viewModel.fotoPerfilSeleccionada) {
                binding.error4.visibility = View.VISIBLE
                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.error4.visibility = View.VISIBLE
                    binding.error4.text = mensaje
                }
            } else {
                binding.error4.visibility = View.INVISIBLE

                val intent = Intent(requireContext(), MainInteriorAplicacion::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode ==  REQUEST_PERMISSION_CAMERA) {
            if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hacerFoto()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun permisosCamara() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            hacerFoto()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
        }
    }

    private fun hacerFoto() {
        val hacerFotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startCameraActivityLauncher.launch(hacerFotoIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e("CATCH HACER FOTO: ", e.message.toString())
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        activityResultLauncher.launch(intent)
    }
}