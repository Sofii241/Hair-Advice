package com.example.escaner.ui.registro.Fragments.Fragments

//noinspection SuspiciousImport

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.databinding.CardviewCiudadBinding
import com.example.escaner.databinding.SpinnerItemBinding
import com.example.escaner.ui.registro.Fragments.FotoPerfilFragment
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ProvinciaFragment : Fragment(), OnItemSelectedListener{

    companion object {
        fun newInstance() = ProvinciaFragment()
    }

    private val viewModel: RegistroViewModel by activityViewModels()

    private lateinit var binding: CardviewCiudadBinding
    private lateinit var bindingSpiner: SpinnerItemBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewCiudadBinding.inflate(layoutInflater)
        bindingSpiner = SpinnerItemBinding.inflate(layoutInflater)

        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSpinnerProvincias()

        binding.next5.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<FotoPerfilFragment>(com.example.escaner.R.id.contenedor_fragments)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    private fun loadSpinnerProvincias() {
        val provincias = viewModel.arrayProvincias()

        val adapter = ArrayAdapter.createFromResource(requireContext(), provincias, R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        binding.spProvincia.adapter = adapter

        binding.spProvincia.onItemSelectedListener = this
        binding.spLocalidad.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val localidadesViewModel = viewModel.arrayLocalidades()
        binding.txtErrorPronvincia.visibility = View.INVISIBLE

        when (parent!!.id) {
            binding.spProvincia.id -> {
                val arrayLocalidades = resources.obtainTypedArray(localidadesViewModel)
                val localidades = arrayLocalidades.getTextArray(position)
                arrayLocalidades.recycle()

                val adapter: ArrayAdapter<CharSequence> = ArrayAdapter<CharSequence>(requireContext(), R.layout.simple_spinner_item, R.id.text1, localidades)

                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.spLocalidad.adapter = adapter

                viewModel.registrarProvinciaSeleccionada(binding.spProvincia.selectedItem.toString())

                binding.txtErrorPronvincia.visibility = View.INVISIBLE
                Log.d("provincia", binding.spProvincia.selectedItem.toString())
            }

            binding.spLocalidad.id -> {
                viewModel.registrarLocalidadSeleccionada(binding.spLocalidad.selectedItem.toString())

                binding.txtErrorPronvincia.visibility = View.INVISIBLE
                Log.d("localidad", binding.spLocalidad.selectedItem.toString())
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        binding.txtErrorPronvincia.visibility = View.VISIBLE
        binding.txtErrorPronvincia.text = viewModel.mensajeErrorProvincia()
    }
}