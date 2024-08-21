package com.example.livrodereceita

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListadeReceitas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listade_receitas)
        var listareceita: ArrayList<Receita> = arrayListOf<Receita>()
        listareceita.add(Receita().apply { this.titulo = "banana"
        this.autor = "Karen Bachini"})
        listareceita.add(Receita().apply { this.titulo = "chocolate"
            this.autor = "Karen Bachini"})
        listareceita.add(Receita().apply { this.titulo = "insulina"
            this.autor = "Karen Bachini"})
        val ReceitaAdapter = ReceitaAdapter(this, listareceita)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        var RecyclerReceita: RecyclerView = findViewById(R.id.lista)
        RecyclerReceita.layoutManager = linearLayoutManager
        RecyclerReceita.adapter = ReceitaAdapter

        var btnaddlista: FloatingActionButton = findViewById(R.id.btnaddlista)
        btnaddlista.setOnClickListener {
            var passatela = Intent(this, Add_receita::class.java)
            startActivity(passatela)
        }
    }
}
class ReceitaAdapter(private val context: Context, ReceitaArray: ArrayList<Receita>) :
    RecyclerView.Adapter<ReceitaAdapter.ViewHolder>() {
    var lista = ReceitaArray
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_lista_receita, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceitaAdapter.ViewHolder, position: Int) {
        val model: Receita = lista[position]
        holder.titulo.setText(model.titulo)
        holder.autor.setText("Autor: " + model.autor)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val titulo: TextView
        public val autor: TextView
        init {
            titulo = itemView.findViewById(R.id.tituloreceita)
            autor = itemView.findViewById(R.id.autorreceita)
        }
    }
}