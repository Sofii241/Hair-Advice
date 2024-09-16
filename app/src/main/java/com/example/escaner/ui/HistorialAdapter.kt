package com.example.escaner.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.escaner.Model.Productos.ItemsHistorial
import com.example.escaner.Model.Productos.Producto
import com.example.escaner.Model.Productos.ProductoManager
import com.example.escaner.R
import com.example.escaner.db.DBHelper
import com.example.escaner.ui.InteriorAplicacion.home.HomeFragment
import com.example.escaner.ui.scanner.ProductView

class HistorialAdapter(private val historialItems: List<ItemsHistorial>) : RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

//    ordenar los productos por la fecha de escaneo
    private var itemsOrdenados: MutableList<ItemsHistorial> = historialItems.sortedByDescending { it.fechaEscaneado }.toMutableList()

    private lateinit var context: Context

    private var onItemDeletedListener: OnItemDeletedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)

        context = view.context

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < itemsOrdenados.size) { // Verificar si la posición es válida
            val item = itemsOrdenados[position]
            holder.bind(item)

            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, ProductView::class.java)
                intent.putExtra("PRODUCT_ID", item.idProducto)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsOrdenados.size
    }

    inner class ViewHolder(itemVista: View) : RecyclerView.ViewHolder(itemVista) {
        private val imageView: ImageView = itemView.findViewById(R.id.imagenProducto)
        private val textView: TextView = itemVista.findViewById(R.id.nombreProducto)
        private val btnEliminar: ImageButton = itemVista.findViewById(R.id.btnEliminarHistorial)

        @SuppressLint("SetTextI18n")
        fun bind(historial: ItemsHistorial) {
            val producto: Producto? = ProductoManager.getProductoById(historial.idProducto)

            if (producto != null) {
                imageView.setImageResource(producto.idImagenProducto)
                textView.text = "${historial.nombreProducto} - ${historial.fechaEscaneado}"

                btnEliminar.setOnClickListener {
                    eliminarItem(adapterPosition)
                }
            }
        }

        private fun eliminarItem(position: Int) {
            if (position != RecyclerView.NO_POSITION && position < itemCount) {
                val dbHelper = DBHelper(itemView.context)
                val idProducto = itemsOrdenados[position].id
                dbHelper.eliminarEscaneado(idProducto.toString())
                itemsOrdenados.removeAt(position)

                notifyItemRemoved(position)

                itemsOrdenados = dbHelper.getEscaneados().sortedByDescending { it.fechaEscaneado }.toMutableList()
                onItemDeletedListener?.onItemDeleted(position)
            }
        }
    }

    fun setOnItemDeletedListener(listener: OnItemDeletedListener) {
        onItemDeletedListener = listener
    }

    class decoracionHistorial(private val altura: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(rect: Rect, view: View, parent: RecyclerView, estado: RecyclerView.State) {
            super.getItemOffsets(rect, view, parent, estado)
            rect.bottom = altura
        }
    }

    interface OnItemDeletedListener {
        fun onItemDeleted(position: Int)
    }
}