package com.example.livrodereceita

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Add_receita : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_receita)
        var btncad: Button = findViewById(R.id.btntlcadastro)
        var titulo: EditText = findViewById(R.id.titulo)
        var autor: EditText = findViewById(R.id.autor)
        var ingredientes: EditText = findViewById(R.id.ingredientes)
        var passos: EditText = findViewById(R.id.passos)

        btncad.setOnClickListener {
            if (titulo.text.isNullOrEmpty() || autor.text.isNullOrEmpty() || ingredientes.text.isNullOrEmpty() || passos.text.isNullOrEmpty()){
                Toast.makeText(this@Add_receita, "Campos vazios", Toast.LENGTH_LONG).show()
            }
            else{
                var receita = Receita()
                receita.titulo = titulo.text.toString()
                receita.autor = autor.text.toString()
                receita.ingredientes = ingredientes.text.toString()
                receita.passos = passos.text.toString()

                var arrayreceita: ArrayList<Receita> = Cache().getReceita(this@Add_receita)
                arrayreceita.add(receita)
                Cache().setReceita(this@Add_receita, arrayreceita)

                finish()
            }
        }
    }
}