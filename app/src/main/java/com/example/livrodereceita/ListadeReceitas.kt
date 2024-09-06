package com.example.livrodereceita

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListadeReceitas : AppCompatActivity() {
    var RecyclerReceita: RecyclerView? = null
    var listareceita = arrayListOf<Receita>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listade_receitas)
        listareceita = Cache().getReceita(context = this@ListadeReceitas)
//        var listareceita: ArrayList<Receita> = arrayListOf<Receita>()
//        listareceita.add(Receita().apply { this.titulo = "banana"
//        this.autor = "Karen Bachini"})
//        listareceita.add(Receita().apply { this.titulo = "chocolate"
//            this.autor = "Karen Bachini"})
//        listareceita.add(Receita().apply { this.titulo = "insulina"
//            this.autor = "Karen Bachini"})
        val ReceitaAdapter = object: ReceitaAdapter(this,this.baseContext, listareceita){
            override fun deletarreceita(id: Long?) {
                var listacache = Cache().getReceita(this@ListadeReceitas)
                listacache.removeIf{
                    it.id == id
                }
                Cache().setReceita(this@ListadeReceitas, listacache)
                listareceita = listacache
            }
        }
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RecyclerReceita = findViewById(R.id.lista)
        RecyclerReceita?.layoutManager = linearLayoutManager
        RecyclerReceita?.adapter = ReceitaAdapter


        var btnshow: FloatingActionButton = findViewById(R.id.btnshow)
        var btncadlista: FloatingActionButton = findViewById(R.id.btncadlista)
        var btnaltlista: FloatingActionButton = findViewById(R.id.btnaltlista)
        var btndellista: FloatingActionButton = findViewById(R.id.btndellista)

        btnaltlista.visibility = View.INVISIBLE
        btncadlista.visibility = View.INVISIBLE
        btndellista.visibility = View.INVISIBLE
        //var count: Int = 0
        btnshow.setOnClickListener {
            var passatela = Intent(this, Add_receita::class.java)
            startActivity(passatela)
            //count++

            //if (count % 2 == 0){
                //btnaltlista.visibility = View.INVISIBLE
                //btncadlista.visibility = View.INVISIBLE
                //btndellista.visibility = View.INVISIBLE
            //}
            //else{

                //btnaltlista.visibility = View.VISIBLE
                //btncadlista.visibility = View.VISIBLE
                //btndellista.visibility = View.VISIBLE
            //}
        }

        //btncadlista.setOnClickListener {
            //var passatela = Intent(this, Add_receita::class.java)
            //startActivity(passatela)

        //}
       // btnaltlista.setOnClickListener {
            // passatela = Intent(this, Add_receita::class.java)
            //startActivity(passatela)

        //}
        //btndellista.setOnClickListener {
            //var passatela = Intent(this, Add_receita::class.java)
            //startActivity(passatela)

        //}
        

    }

    override fun onResume() {
        super.onResume()
        listareceita = Cache().getReceita(context = this@ListadeReceitas)
        (RecyclerReceita?.adapter as ReceitaAdapter)?.attlista(listareceita)
        (RecyclerReceita?.adapter as ReceitaAdapter)?.notifyDataSetChanged()
        //RecyclerReceita?.adapter = ReceitaAdapter(this,this.baseContext, listareceita)
    }
}

abstract class ReceitaAdapter(private val activity: ListadeReceitas, private val context: Context, private var lista: ArrayList<Receita>) :
    RecyclerView.Adapter<ReceitaAdapter.ViewHolder>() {
    abstract fun deletarreceita(id:Long?)
     fun attlista(listadeReceitas: ArrayList<Receita>){
         lista = listadeReceitas
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_lista_receita, parent, false)
        return ViewHolder(view)
    }
    

    override fun onBindViewHolder(holder: ReceitaAdapter.ViewHolder, position: Int) {
        val model: Receita = lista[position]
        holder.titulo.setText(model.titulo)
        holder.autor.setText("Autor: " + model.autor)
        holder.card.setOnClickListener {
            activity.startActivity (Intent(context, Add_receita::class.java).putExtra("id", model.id))
        }
        holder.card.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                var dgl = AlertDialog.Builder(activity, R.style.Theme_LivroDeReceita)
                .setMessage("Tem certeza que deseja deletar?")
                .setPositiveButton("Sim", object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        deletarreceita(model.id)

                        Toast.makeText(context, "Removido com sucesso", Toast.LENGTH_LONG).show()
                        dialog?.dismiss()
                        lista.removeAt(position)
                        notifyItemRemoved(position)
                    }

                })
                .setNegativeButton("Não", object: DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                        }

                    })
                .create()
                dgl.show()
                return true
            }

        })


    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val titulo: TextView
        public val autor: TextView
        public val card: CardView
        init {
            titulo = itemView.findViewById(R.id.tituloreceita)
            autor = itemView.findViewById(R.id.autorreceita)
            card = itemView.findViewById(R.id.cardview)
        }

    }
}