package com.example.escaner.ui.scanner

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.escaner.Model.Productos.ProductoManager
import com.example.escaner.R
import com.example.escaner.databinding.ActivityScanBinding
import com.example.escaner.db.DBHelper
import com.example.escaner.ui.InteriorAplicacion.home.HomeFragment
import com.example.escaner.ui.MainInteriorAplicacion
import com.example.escaner.ui.registrocorreo.RegistroCorreoFragment
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        initEscanner()
    }

    private fun initEscanner() {
        val integrator = IntentIntegrator(this)

        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13)
        integrator.setPrompt("Escanee el código de barras del producto")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        if (result.contents == null) {
            Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            volverPantallaInicio()
            return
        }

        val id = result.contents
        val producto = ProductoManager.getProductoById(id)

        Log.d("PRODUCTO", "PRODUCTO ID $id")
        if(producto == null) {
            Toast.makeText(this, "No existe información del codigo de barras.", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainInteriorAplicacion::class.java)
            startActivity(intent)
            return
        }

        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        dbHelper.escanear(id, currentDate, producto.nombre)

        val intent = Intent(this, ProductView::class.java)
        intent.putExtra("PRODUCT_ID", id)
        startActivity(intent)
    }

    private fun volverPantallaInicio() {
       val intent = Intent(this, MainInteriorAplicacion::class.java)
        startActivity(intent)
    }
}