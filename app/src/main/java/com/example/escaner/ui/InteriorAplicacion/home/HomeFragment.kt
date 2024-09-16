package com.example.escaner.ui.InteriorAplicacion.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.escaner.Model.Productos.ItemsHistorial
import com.example.escaner.Model.Productos.ProductoManager
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.R
import com.example.escaner.databinding.FragmentHomeBinding
import com.example.escaner.db.DBHelper
import com.example.escaner.ui.HistorialAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(), HistorialAdapter.OnItemDeletedListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarDatos()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemDeleted(position: Int) {
        val adapter = binding.recyclerViewHistory.adapter as? HistorialAdapter
        adapter?.notifyItemRemoved(position)
        adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        cargarDatos()
    }

    private fun cargarDatos() {
        val dbHelper = DBHelper(requireContext())
        val historialEscaner = dbHelper.getEscaneados()

        val valoresHistorial = if (historialEscaner.size > 5) {
            historialEscaner.takeLast(7).toList()
        } else {
            historialEscaner.toList()
        }

        val adapter = HistorialAdapter(valoresHistorial)
        adapter.setOnItemDeletedListener(this)
        binding.recyclerViewHistory.adapter = adapter

        val itemDecoration = HistorialAdapter.decoracionHistorial(4)
        binding.recyclerViewHistory.addItemDecoration(itemDecoration)

        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}