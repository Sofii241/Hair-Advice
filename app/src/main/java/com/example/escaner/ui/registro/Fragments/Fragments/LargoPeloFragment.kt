package com.example.escaner.ui.registro.Fragments.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.Model.Usuarios.Enums.Largo
import com.example.escaner.R
import com.example.escaner.databinding.CardviewLargoPeloBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class LargoPeloFragment: Fragment() {

    private lateinit var binding: CardviewLargoPeloBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewLargoPeloBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seleccionBoton()

        binding.next11.setOnClickListener{
            if(!viewModel.comprobacionMarcadoLargoPelo(binding.buttonMuyCorto.isSelected)
                && !viewModel.comprobacionMarcadoLargoPelo(binding.buttonCorto.isSelected)
                && !viewModel.comprobacionMarcadoLargoPelo(binding.buttonMedio.isSelected)
                && !viewModel.comprobacionMarcadoLargoPelo(binding.buttonLargo.isSelected)) {

                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.errorLargoP.visibility = View.VISIBLE
                    binding.errorLargoP.text = mensaje
                }
            } else {
                binding.errorLargoP.visibility = View.INVISIBLE
                requireActivity().supportFragmentManager.commit {
                    replace<TipTextuCondFragment>(R.id.contenedor_fragments)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }

    private fun seleccionBoton() {
        binding.buttonMuyCorto.setOnClickListener { actualizarSeleccion(it) }
        binding.buttonCorto.setOnClickListener { actualizarSeleccion(it) }
        binding.buttonMedio.setOnClickListener { actualizarSeleccion(it) }
        binding.buttonLargo.setOnClickListener { actualizarSeleccion(it) }
    }

    private fun actualizarSeleccion(selectedView: View) {
        val botones = listOf(
            binding.buttonMuyCorto,
            binding.buttonCorto,
            binding.buttonMedio,
            binding.buttonLargo
        )

        // Desmarcar todos los botones
        for (boton in botones) {
            boton.isSelected = false
            viewModel.desmarcarSeleccionPantalla(boton)
        }

        // Marcar el botón seleccionado
        selectedView.isSelected = true
        viewModel.marcarSeleccionPantalla(selectedView as ImageView)


        when (selectedView) {
            binding.buttonMuyCorto -> {
                binding.buttonMuyCorto.isSelected = true
                binding.buttonCorto.isSelected = false
                binding.buttonMedio.isSelected = false
                binding.buttonLargo.isSelected = false

                viewModel.registrarLargo(Largo.MuyCorto)
                viewModel.controlMarcadoPantalla(binding.buttonMuyCorto)
            }

            binding.buttonCorto -> {
                binding.buttonCorto.isSelected = true
                binding.buttonMuyCorto.isSelected = false
                binding.buttonMedio.isSelected = false
                binding.buttonLargo.isSelected = false

                viewModel.registrarLargo(Largo.Corto)
                viewModel.controlMarcadoPantalla(binding.buttonCorto)
            }

            binding.buttonMedio -> {
                binding.buttonMedio.isSelected = true
                binding.buttonMuyCorto.isSelected = false
                binding.buttonCorto.isSelected = false
                binding.buttonLargo.isSelected = false

                viewModel.registrarLargo(Largo.Medio)
                viewModel.controlMarcadoPantalla(binding.buttonMedio)
            }

            binding.buttonLargo -> {
                binding.buttonLargo.isSelected = true
                binding.buttonMuyCorto.isSelected = false
                binding.buttonCorto.isSelected = false
                binding.buttonMedio.isSelected = false

                viewModel.registrarLargo(Largo.Largo)
                viewModel.controlMarcadoPantalla(binding.buttonLargo)
            }
        }

        Log.d("ID MUY CORTO", binding.buttonMuyCorto.id.toString())
        Log.d("ID CORTO", binding.buttonCorto.id.toString())
        Log.d("ID MEDIO", binding.buttonMedio.id.toString())
        Log.d("ID LARGO", binding.buttonLargo.id.toString())

        Log.d("LARGO SELECCIOANDO", selectedView.id.toString())
    }
}