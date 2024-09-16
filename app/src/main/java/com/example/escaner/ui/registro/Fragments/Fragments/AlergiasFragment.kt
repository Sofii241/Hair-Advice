package com.example.escaner.ui.registro.Fragments.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.Util.Resource
import com.example.escaner.databinding.CardviewAlergiasBinding
import com.example.escaner.ui.MainInteriorAplicacion
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlergiasFragment: Fragment() {

    private lateinit var binding: CardviewAlergiasBinding
    private val viewModel: RegistroViewModel by activityViewModels()
    private var currentUser: UserDAO = UserDAO()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewAlergiasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obtenerCurrentUser()
        initObservers()
        initListeners()


    }

    private fun initListeners() {
        binding.next6.setOnClickListener {
            if (viewModel.validarCampoAlergias(binding.editTxtAlergia.text.toString())) {
                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.textViewErr.visibility = View.VISIBLE
                    binding.textViewErr.text = mensaje
                }
            } else if (binding.editTxtAlergia.text.toString().isNotEmpty()) {
                viewModel.registrarAlergia(binding.editTxtAlergia.text.toString())
                handleRegistro(currentUser.id, viewModel.usuario())
            } else {
                handleRegistro(currentUser.id, viewModel.usuario())
            }
        }
    }

    private fun obtenerCurrentUser() {
        viewModel.currentUser.observe(viewLifecycleOwner) { id ->
            if (id!= null) {
                currentUser.id = id.toString()
            }
        }
    }

    private fun handleRegistro(uid:String, user: UserDAO) {
        viewModel.registrarUserFirestore(uid, user)
    }

    private fun initObservers() {
        viewModel.estadoRegistroFirebase.observe(viewLifecycleOwner) { registrado ->
            when (registrado) {
                is Resource.completado -> {
                    mostrarCardViewLoading()
                    Log.d("ALMACENAMIENTO EN FIREBASE OK", viewModel.usuario().toString())
                }

                is Resource.error -> {
                    ocultarCardViewLoading()
                    Log.d("ALMACENAMIENTO EN FIREBASE ERROR", registrado.message)
                }

                else -> Unit
            }
        }
    }

    private fun mostrarCardViewLoading() {
        binding.cardViewFin.visibility = View.VISIBLE
        redireccion()
    }

    private fun ocultarCardViewLoading() {
        binding.cardViewFin.visibility = View.GONE
    }

    private fun redireccion() {
        Handler(Looper.getMainLooper()).postDelayed({
            entraAPP()
        }, 3000)
    }

    private fun entraAPP() {
        val intent = Intent(requireContext(), MainInteriorAplicacion::class.java)
        startActivity(intent)
    }
}