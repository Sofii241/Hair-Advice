package com.example.escaner.ui.registro.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.R
import com.example.escaner.databinding.CardviewFotoPerfilBinding
import com.example.escaner.ui.registro.Fragments.Fragments.EditarFotoFragment
import com.example.escaner.ui.registro.Fragments.Fragments.IntermedioFragment
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class FotoPerfilFragment : Fragment(), View.OnClickListener {

    private val REQUEST_PERMISSION_CAMERA = 100

    companion object {
        fun newInstance() = FotoPerfilFragment()
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

            viewModel.registrarFotoPerfil(true)
            EditarFotoFragment.guardarImagenEnAlmacenamientoInterno(requireContext(), bitmap, "FotoPerfilCamara.jpg")
        }
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val data: Intent? = result.data
            imagenGaleria = data?.data!!

            binding.imageViewProfile.visibility = View.VISIBLE
            binding.imageViewProfile.setImageURI(imagenGaleria)

            viewModel.registrarFotoPerfil(true)
            EditarFotoFragment.guardarImagenEnAlmacenamientoInterno(requireContext(), imagenGaleria, "FotoPerfilGaleria.jpg")
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
            viewModel.registrarFotoPerfil(false)
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
                requireActivity().supportFragmentManager.commit {
                    replace<IntermedioFragment>(R.id.contenedor_fragments)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
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