package com.example.escaner.ui.scanner

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.escaner.Model.Productos.Producto
import com.example.escaner.Model.Productos.ProductoManager
import com.example.escaner.Model.Productos.Recomendacion
import com.example.escaner.Model.Usuarios.Enums.Nivel
import com.example.escaner.Model.Usuarios.UserDAO
import com.example.escaner.R
import com.example.escaner.databinding.ActivityProductoBinding
import com.example.escaner.ui.MainInteriorAplicacion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@Suppress("DEPRECATION")
class ProductView : AppCompatActivity() {

    private lateinit var binding: ActivityProductoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        binding.mainContent.visibility = View.GONE

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid.toString()

        lifecycleScope.launch {
            val user: UserDAO = buscarPorUID(userId)

            val productoId = intent.getStringExtra("PRODUCT_ID")
            productoId?.let {
                val producto = ProductoManager.getProductoById(it)

                if (producto != null) {
                    binding.textViewProductName.text = producto.nombre
                    binding.textViewProductBrand.text = producto.marca
                    binding.imageViewProduct.setImageResource(producto.idImagenProducto)

                    binding.textViewIngredientes.text =
                        producto.ingredientes.joinToString(separator = "\n")

                    binding.textViewRecomendaciones.text = obtenerRecomendaciones(producto, user)
                    binding.textViewUso.text = producto.uso.joinToString(separator = "\n")
                }
            }

            binding.progressBar.visibility = View.GONE
            binding.mainContent.visibility = View.VISIBLE
        }

        setupExpandableSection(binding.buttonIngredientes, binding.textViewIngredientes)
        setupExpandableSection(binding.buttonRecomendaciones, binding.textViewRecomendaciones)
        setupExpandableSection(binding.buttonUso, binding.textViewUso)
    }

    private fun setupExpandableSection(button: Button, textView: View) {
        button.setOnClickListener {
            if (textView.visibility == View.GONE) {
                textView.visibility = View.VISIBLE
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            } else {
                textView.visibility = View.GONE
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainInteriorAplicacion::class.java)
        startActivity(intent)
        finish()
    }

    private fun obtenerRecomendaciones(producto: Producto, user: UserDAO): SpannableString {
        val recomendaciones = SpannableStringBuilder()

        fun addRecomendacion(recomendacion: Recomendacion) {
            val color = when (recomendacion.nivel) {
                Nivel.Recomendado -> Color.GREEN
                Nivel.NoRecomendado -> Color.RED
                else -> Color.YELLOW
            }

            val squareBitmap = createColoredSquareWithBorder(color)
            val spannableRecomendacion = SpannableString("  " + recomendacion.recomendacion + "\n\n")
            spannableRecomendacion.setSpan(ImageSpan(this, squareBitmap), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            recomendaciones.append(spannableRecomendacion)
        }
        producto.recomendaciones.find { reco -> reco.id == user.tenido.name }?.let {
            addRecomendacion(it)
        }
        producto.recomendaciones.find { reco -> reco.id == user.peloTextura.name }?.let {
            addRecomendacion(it)
        }
        producto.recomendaciones.find { reco -> reco.id == user.peloCondicion.name }?.let {
            addRecomendacion(it)
        }
        producto.recomendaciones.find { reco -> reco.id == user.tipoTratamiento.name }?.let {
            addRecomendacion(it)
        }

        return SpannableString(recomendaciones)
    }

    private fun createColoredSquareWithBorder(color: Int): Bitmap {
        val size = 40
        val borderSize = 2
        val bitmap = Bitmap.createBitmap(size + 2 * borderSize, size + 2 * borderSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw the border
        val paint = Paint()
        paint.color = Color.GRAY
        canvas.drawRect(0f, 0f, (size + 2 * borderSize).toFloat(), (size + 2 * borderSize).toFloat(), paint)

        // Draw the colored square inside the border
        paint.color = color
        canvas.drawRect(borderSize.toFloat(), borderSize.toFloat(), (size + borderSize).toFloat(), (size + borderSize).toFloat(), paint)

        return bitmap
    }

    suspend fun buscarPorUID(iud: String): UserDAO = withContext(Dispatchers.IO) {
        val provider: FirebaseFirestore = FirebaseFirestore.getInstance()

        try {
            val document = provider.collection("usuarios").document(iud).get().await()
            if (document.exists()) {
                document.toObject(UserDAO::class.java) ?: UserDAO()
            } else {
                Log.d("USER OBTENIDO REPOSITORIO", "No existe el usuario con UID: $iud")
                UserDAO()
            }
        } catch (e: Exception) {
            Log.d("ERROR AL BUSCAR ID USER", "${e.message}")
            UserDAO()
        }
    }
}