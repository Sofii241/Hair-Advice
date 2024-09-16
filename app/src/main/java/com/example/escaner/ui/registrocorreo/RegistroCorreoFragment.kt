package com.example.escaner.ui.registrocorreo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.escaner.R
import com.example.escaner.Util.Resource
import com.example.escaner.databinding.CardviewRegistroCorreoBinding
import com.example.escaner.ui.registro.Fragments.Fragments.BienvenidaFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistroCorreoFragment: Fragment() {

    private val viewModel: RegistroCorreoViewModel by viewModels()
    private lateinit var binding: CardviewRegistroCorreoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewRegistroCorreoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
        observadorVerificacion()

    }



    private fun initObservers() {
        viewModel.estadoAcceso.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                is Resource.completado -> {
                    handleLoading(false)
                    iniciarFragmentRegistro()
                    Log.d("REGISTRO", "SUCCESS")
                }

                is Resource.error -> {
                    handleLoading(false)
                    Log.d("REGISTRO ERROR", estado.message)
                }

                is Resource.cargando -> handleLoading(true)

                else -> Unit
            }
        }
    }

    private fun initListeners() {
        binding.next9.setOnClickListener {
            validacion()
        }
    }

    private fun validacion() {
        if (viewModel.validarCorreo(binding.emailEditText.text.toString())
            && viewModel.validarClave(binding.passwordEditText.text.toString())
            && viewModel.validarConfirmacionClave(binding.passwordEditText.text.toString(), binding.confirmPasswordEditText.text.toString())) {

            handleRegistro(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
            ocultarError()
        } else {
             viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                 if (mensaje != null) {
                     mostrarError(mensaje)
                 } else {
                     ocultarError()
                 }
             }
        }
    }

    private fun handleRegistro(email: String, password: String){
        viewModel.registro(email, password,
            registrado = {
                mostrarCardViewEnlaceVerificacion()
                handleLoading(true)
            },
            fallo = { e ->
                handleLoading(false)
                ocultarError()
                ocultarCardViewEnlaceVerificacion()
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
            })
    }

    private fun mostrarCardViewEnlaceVerificacion() {
        binding.cardEnlaceEnviado.visibility = View.VISIBLE
        binding.cardRegistro.visibility = View.GONE
    }
    private fun ocultarCardViewEnlaceVerificacion() {
        binding.cardEnlaceEnviado.visibility = View.GONE
        binding.cardRegistro.visibility = View.VISIBLE
    }

    private fun observadorVerificacion() {
        viewModel.correoVerificado.observe(viewLifecycleOwner, Observer { isVerified ->
            if (isVerified) {
                binding.progressBarEnlace.visibility = View.GONE
                Toast.makeText(requireContext(), "Correo verificado correctamente", Toast.LENGTH_SHORT).show()
                iniciarFragmentRegistro()
            }
        })
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.next9.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.next9.isEnabled = true
        }
    }

    private fun mostrarError(mensaje:String){
        binding.error6.visibility = View.VISIBLE
        binding.error6.text = mensaje
    }

    private fun ocultarError(){
        binding.error6.visibility = View.INVISIBLE
    }

    private fun iniciarFragmentRegistro(){
        requireActivity().supportFragmentManager.commit {
            replace<BienvenidaFragment>(R.id.contenedor_fragments)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}