package com.example.escaner.ui.registro.Fragments.Fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.R
import com.example.escaner.databinding.CardviewFechaNacimientoBinding
import com.example.escaner.ui.registro.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class FechaNacimientoFragment : Fragment() {

    companion object {
        fun newInstance() = FechaNacimientoFragment()
    }

    private lateinit var binding: CardviewFechaNacimientoBinding
    private val viewModel: RegistroViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = CardviewFechaNacimientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconoCalendario.setOnClickListener {
            mostrarCalendario()
        }

        binding.next6.setOnClickListener {
            val fechaNacimientoText = binding.seleccionFechaNacimiento.text.toString()
            if (!viewModel.comprobacionCampoFechaVacio(fechaNacimientoText)) {
                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.txtFechaVacia.visibility = View.VISIBLE
                    binding.txtFechaVacia.text = mensaje
                }
            } else if (!viewModel.comprobacionCaracteresFecha(fechaNacimientoText)) {
                viewModel.mensajeError.observe(viewLifecycleOwner) { mensaje ->
                    binding.txtFechaVacia.visibility = View.VISIBLE
                    binding.txtFechaVacia.text = mensaje
                }
            } else {
                binding.txtFechaVacia.visibility = View.INVISIBLE

                Log.d("NOMBRE", viewModel.usuario().nombre)
                Log.d("APELLIDO", viewModel.usuario().apellido)
                Log.d("FECHA NACIMIENTO", viewModel.usuario().fechaNacimiento.toString())

                requireActivity().supportFragmentManager.commit {
                    replace<GeneroFragment>(R.id.contenedor_fragments)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }

    private fun mostrarCalendario() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val fechaFormateada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)

                viewModel.registrarFechaSeleccionada(calendar.time)
                binding.seleccionFechaNacimiento.setText(fechaFormateada)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}