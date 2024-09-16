package com.example.escaner.ui.registro.Fragments.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.R
import com.example.escaner.databinding.CardviewTratamientoBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TratamientoPeloFragment: Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: CardviewTratamientoBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewTratamientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadTipoTratamiento()

        binding.checkSiTratamiento.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkNoTratamiento.isChecked = false
                binding.textViewTratamiento.visibility = View.VISIBLE
                binding.linearSpinner.visibility = View.VISIBLE
                binding.errorTratamiento.visibility = View.INVISIBLE

                viewModel.registrarSiPeloTratamiento(true)
            } else {
                binding.textViewTratamiento.visibility = View.GONE
                binding.linearSpinner.visibility = View.GONE
            }
        }

        binding.checkNoTratamiento.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkSiTratamiento.isChecked = false
                binding.textViewTratamiento.visibility = View.GONE
                binding.linearSpinner.visibility = View.GONE
                binding.errorTratamiento.visibility = View.INVISIBLE

                viewModel.registrarSiPeloTratamiento(false)
            }
        }

        binding.next14.setOnClickListener {
            if (!viewModel.comprobacionCheckMarcadoTratamiento(binding.checkSiTratamiento.isChecked, binding.checkNoTratamiento.isChecked)){
                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.errorTratamiento.visibility = View.VISIBLE
                    binding.errorTratamiento.text = mensaje
                }
            }

            if(binding.checkSiTratamiento.isSelected){
                if (!viewModel.comprobacionSeleccionTipoTratamiento(binding.spinerTratamientoPelo.isSelected)){
                    viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                        binding.errorTratamiento.visibility = View.VISIBLE
                        binding.errorTratamiento.text = mensaje
                    }
                }
            } else {
                binding.errorTratamiento.visibility = View.INVISIBLE
                requireActivity().supportFragmentManager.commit {
                    replace<AlergiasFragment>(R.id.contenedor_fragments)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }


    private fun loadTipoTratamiento() {
        val tratamiento = viewModel.listaTratamientos()

        val adapter = ArrayAdapter.createFromResource(requireContext(), tratamiento, R.layout.simple_spinner_item_pelo)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinerTratamientoPelo.adapter = adapter
        binding.spinerTratamientoPelo.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id) {
            binding.spinerTratamientoPelo.id -> {
                viewModel.registrarTipoTratamiento(binding.spinerTratamientoPelo.selectedItem.toString())
                Log.d("TRATAMIENTO", binding.spinerTratamientoPelo.selectedItem.toString())
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        binding.textViewTratamiento.visibility = View.INVISIBLE
        binding.textViewTratamiento.text = viewModel.mensajeErrorSpinerTratamientoPelo()
    }
}