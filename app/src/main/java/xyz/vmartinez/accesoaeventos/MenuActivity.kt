package xyz.vmartinez.accesoaeventos

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class MenuActivity : AppCompatActivity() {

    lateinit var spinner: Spinner
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById<ImageView>(R.id.imageView2)
        spinner = findViewById<Spinner>(R.id.spinner)

        llenarElSpinner()
    }

    private fun llenarElSpinner() {
        var lista = ArrayList<String>()
        lista.add("Seleccione")
        lista.add("Evento 1")
        lista.add("Evento 2")
        var adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            lista
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != 0){

                val nombreDelEvento = lista[position]
                Toast.makeText(this@MenuActivity,nombreDelEvento, Toast.LENGTH_LONG).show()

                val uri = "https://utilidades.vmartinez84.xyz/api/Servicios?mensaje="+ nombreDelEvento
                Glide.with(this@MenuActivity).load(uri).into(imageView)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }
        }
    }
}