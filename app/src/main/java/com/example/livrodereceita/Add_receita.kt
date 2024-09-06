package com.example.livrodereceita

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Add_receita : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_receita)
        val id = intent.getLongExtra("id", 0)
        val itemreceita = Cache().id(this@Add_receita, id)

        var btncad: Button = findViewById(R.id.btntlcadastro)
        var titulo: EditText = findViewById(R.id.titulo)
        var autor: EditText = findViewById(R.id.autor)
        var ingredientes: EditText = findViewById(R.id.ingredientes)
        var passos: EditText = findViewById(R.id.passos)
        var titulotela:TextView = findViewById(R.id.activitytitle)
        var btnalt: FloatingActionButton = findViewById(R.id.btnalt)

        btnalt.isInvisible = true

        if (itemreceita != null){
            autor.setText(itemreceita.autor)
            titulo.setText(itemreceita.titulo)
            passos.setText(itemreceita.passos)
            ingredientes.setText(itemreceita.ingredientes)
            autor.isEnabled = false
            titulo.isEnabled = false
            passos.isEnabled = false
            ingredientes.isEnabled = false
            btncad.isInvisible = true
            titulotela.text = "Receita"
            btnalt.isInvisible = false
            btncad.text = "Alterar Receita"
        }

        btnalt.setOnClickListener {
            btncad.isInvisible = false
            autor.isEnabled = true
            titulo.isEnabled = true
            passos.isEnabled = true
            ingredientes.isEnabled = true
            btnalt.isInvisible = true
        }

        btncad.setOnClickListener {
            if (titulo.text.isNullOrEmpty() || autor.text.isNullOrEmpty() || ingredientes.text.isNullOrEmpty() || passos.text.isNullOrEmpty()){
                Toast.makeText(this@Add_receita, "Campos vazios", Toast.LENGTH_LONG).show()
            }
            else{
                var receita = if (itemreceita != null) itemreceita else Receita()
                receita.titulo = titulo.text.toString()
                receita.autor = autor.text.toString()
                receita.ingredientes = ingredientes.text.toString()
                receita.passos = passos.text.toString()
                if (itemreceita == null){
                    receita.id = Cache().countid(this@Add_receita)
                }


                var arrayreceita: ArrayList<Receita> = Cache().getReceita(this@Add_receita)
                if (itemreceita != null){
                    arrayreceita.removeIf{
                        it.id == itemreceita.id
                    }
                    arrayreceita.add(receita)
                }
                else{
                    arrayreceita.add(receita)
                }

                Cache().setReceita(this@Add_receita, arrayreceita)

                finish()
            }
        }
    }
}