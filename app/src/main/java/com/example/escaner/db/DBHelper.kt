package com.example.escaner.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.escaner.Model.Productos.ItemsHistorial

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, BD_NOMBRE, null, BD_VERSION) {

    companion object {
        private const val BD_VERSION = 1
        private const val BD_NOMBRE = "histscans"
        private const val NOMBRE_TABLA = "histscans"
        private const val CLAVE_ID = "id"
        private const val ID_PRODUCTO = "id_producto" // id del producto
        private const val CLAVE_FECHA = "fecha"
        private const val NOMBRE_PRODUCTO = "nombre"
    }

    override fun onCreate(bd: SQLiteDatabase?) {
        val crearTabla = ("CREATE TABLE $NOMBRE_TABLA ($CLAVE_ID INTEGER PRIMARY KEY AUTOINCREMENT, $ID_PRODUCTO TEXT, $CLAVE_FECHA TEXT, $NOMBRE_PRODUCTO TEXT)")
        bd?.execSQL(crearTabla)
    }

//   para actualizar version de la base de datos en caso de cambios
    override fun onUpgrade(bd: SQLiteDatabase?, versionAnterior: Int, nuevaVersion: Int) {
        bd?.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA")
        onCreate(bd)
    }

    fun eliminarEscaneado(claveId: String) {
        val bd = this.writableDatabase
        bd.delete(NOMBRE_TABLA, "$CLAVE_ID=?", arrayOf(claveId))
        bd.close()
    }

    fun escanear(idProducto: String, fecha: String, nombre: String) {
        val bd = this.writableDatabase
        val valores = ContentValues()
        valores.put(ID_PRODUCTO, idProducto)
        valores.put(CLAVE_FECHA, fecha)
        valores.put(NOMBRE_PRODUCTO, nombre)
        bd.insert(NOMBRE_TABLA, null, valores)
        bd.close()
    }

    @SuppressLint("Range")
    fun getEscaneados(): ArrayList<ItemsHistorial> {
        val listaEscaneados = ArrayList<ItemsHistorial>()
        val select = "SELECT * FROM $NOMBRE_TABLA"
        val bd = this.readableDatabase
        val cursor = bd.rawQuery(select, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(CLAVE_ID))
                val idProducto = cursor.getString(cursor.getColumnIndex(ID_PRODUCTO))
                val fecha = cursor.getString(cursor.getColumnIndex(CLAVE_FECHA))
                val nombre = cursor.getString(cursor.getColumnIndex(NOMBRE_PRODUCTO))

                Log.d("DATABASE", "$id $idProducto $fecha $nombre")

                listaEscaneados.add(ItemsHistorial(id, idProducto, nombre, fecha))
            } while (cursor.moveToNext())
        }
        cursor.close()
        bd.close()
        return listaEscaneados
    }
}