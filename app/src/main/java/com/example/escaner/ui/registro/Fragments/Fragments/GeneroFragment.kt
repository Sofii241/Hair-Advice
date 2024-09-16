package com.example.escaner.ui.registro.Fragments.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.Model.Usuarios.Enums.Genero
import com.example.escaner.R
import com.example.escaner.databinding.CardviewGeneroBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneroFragment: Fragment() {

    private lateinit var binding: CardviewGeneroBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewGeneroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controlSeleccion()
        validacion()
    }

    private fun validacion() {
        binding.next8.setOnClickListener {
            if (viewModel.comprobacionCheckMarcadoMujer(binding.rBMujer.isChecked)) {
                viewModel.registrarGeneroSeleccionado(Genero.Femenino)
                cambioFragment()
            } else if (viewModel.comprobacionCheckMarcadoHombre(binding.rBHombre.isChecked)) {
                viewModel.registrarGeneroSeleccionado(Genero.Masculino)
                cambioFragment()
            } else if (viewModel.comprobacionCheckMarcadoNo(binding.rBNoEspecifica.isChecked)) {
                viewModel.registrarGeneroSeleccionado(Genero.Indefinido)
                cambioFragment()
            } else {
                observador()
            }
        }
    }

    private fun cambioFragment() {
        Log.d("NOMBRE ", viewModel.usuario().nombre)
        Log.d("NOMBRE ", viewModel.usuario().apellido)
        Log.d("NOMBRE ", viewModel.usuario().genero.name)
        Log.d("NOMBRE ", viewModel.usuario().fechaNacimiento.toString())

        requireActivity().supportFragmentManager.commit {
            replace<ProvinciaFragment>(R.id.contenedor_fragments)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun observador(){
        viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
            binding.error3.visibility = View.VISIBLE
            binding.error3.text = mensaje
        }
    }

    private fun controlSeleccion(){
        binding.rBMujer.setOnClickListener {
            binding.rBHombre.isChecked = false
            binding.rBNoEspecifica.isChecked = false
        }
        binding.rBHombre.setOnClickListener {
            binding.rBMujer.isChecked = false
            binding.rBNoEspecifica.isChecked = false
        }
        binding.rBNoEspecifica.setOnClickListener {
            binding.rBMujer.isChecked = false
            binding.rBHombre.isChecked = false
        }
    }
}