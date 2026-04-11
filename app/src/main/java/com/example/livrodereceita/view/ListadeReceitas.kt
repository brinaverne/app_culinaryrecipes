package com.example.livrodereceita.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.MetricAffectingSpan
import android.text.style.StyleSpan
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.livrodereceita.R
import com.example.livrodereceita.model.Receita
import com.example.livrodereceita.viewmodel.ReceitaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


class ListadeReceitas : AppCompatActivity() {
    lateinit var viewModel: ReceitaViewModel
    lateinit var searchRecipe: TextInputEditText
    var RecyclerReceita: RecyclerView? = null
    var listareceita = arrayListOf<Receita>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listade_receitas)

        //var toolbar: Toolbar = findViewById(R.id.lista_receita_toolbar)
        //setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar)

        viewModel = ViewModelProvider(this).get(ReceitaViewModel::class.java)


        RecyclerReceita = findViewById(R.id.lista)
        RecyclerReceita?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        viewModel.select()
        viewModel.listAllReceita.observe(this, Observer {
            RecyclerReceita?.adapter = object: ReceitaAdapter(it, this@ListadeReceitas){
                override fun updateReceita(id: Long) {
                    var intent = Intent(this@ListadeReceitas, Add_receita::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                }

                override fun deletarreceita(id: Long) {
                    viewModel.delete(id)
                }

            }
        })


        var btnshow: FloatingActionButton = findViewById(R.id.btnshow)



        btnshow.setOnClickListener {
            var passatela = Intent(this, Add_receita::class.java)
            startActivity(passatela)

        }

        var btnMenu: ImageButton = findViewById(R.id.btn_menu_lista_receita)
        btnMenu.setOnClickListener {
            val popup = PopupMenu(this, btnMenu, 0, 0, R.style.MyPopupMenuStyle)
            popup.menuInflater.inflate(R.menu.menu_lista_receita, popup.menu)

            for (i in 0 until popup.menu.size()) {
                val item = popup.menu.getItem(i)
                val spanString = SpannableString(item.title.toString())
                item.title = spanString


                val color = ContextCompat.getColor(this@ListadeReceitas, R.color.white)
                val typeface = ResourcesCompat.getFont(this@ListadeReceitas, R.font.kgprimarypenmanship)

                spanString.setSpan(ForegroundColorSpan(color), 0, item.title.toString().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                typeface?.let {
                    spanString.setSpan(CustomTypefaceSpan(it), 0, item.title.toString().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                spanString.setSpan(StyleSpan(Typeface.BOLD), 0, item.title.toString().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                spanString.setSpan(AbsoluteSizeSpan(22, true), 0, item.title.toString().length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.opcao_pesquisar_receita -> {
                        var passatela = Intent(this, Add_receita::class.java)
                        passatela.putExtra("isSearching", true)
                        startActivity(passatela)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }

        searchRecipe = findViewById(R.id.search_recipe)
        searchRecipe.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(text: Editable?) {
                val texto = text.toString()

                if(!texto.isBlank()){
                    searchDataBase(texto)
                } else{
                    viewModel.select()
                }

            }
        })
        /*searchRecipe.setOnQueryTextListener(object  : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if(!query.isNullOrEmpty()){
                   searchDataBase(query)

                } else {
                    Toast.makeText(this@ListadeReceitas,"Preencha o campo de busca!", Toast.LENGTH_SHORT).show()
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("merda", "novo valor $newText")
                if(newText.isNullOrEmpty()){
                    viewModel.select()
                } else{
                    searchDataBase(newText)
                }

                return false
            }

        })

         */



    }


    //override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //   menuInflater.inflate(R.menu.menu_lista_receita, menu)
    //    return super.onCreateOptionsMenu(menu)
    //}

    //override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    //    return super.onPrepareOptionsMenu(menu)
    //}

    override fun onResume() {
        super.onResume()
        viewModel.select()

    }

    fun searchDataBase(query:String){
        val searchQuery = "%$query%"
        viewModel.selectByName(searchQuery)

    }
}




abstract class ReceitaAdapter(lista: List<Receita>, context: Context) : RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder>() {

    private var recipeList = lista.toMutableList()
    private val cont = context
    abstract fun deletarreceita(id:Long)

    abstract fun updateReceita(id: Long)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaViewHolder {
        val cardview: View = LayoutInflater.from(parent.context).inflate(R.layout.cardview_lista_receita, parent, false)
        return ReceitaViewHolder(cardview)
    }
    

    override fun onBindViewHolder(holder: ReceitaViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.titulo.text = recipeList.get(position).titulo
        holder.autor.setText(String.format(cont.getString(R.string.autor_da_receita_card), recipeList.get(position).autor))
        holder.card.setOnClickListener {
            updateReceita(recipeList[position].id)
        }
        holder.card.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                var dgl = AlertDialog.Builder(cont)
                .setTitle(cont.getString(R.string.deseja_deletar))
                .setPositiveButton(cont.getString(R.string.sim), object: DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        deletarreceita(recipeList.get(position).id)

                        Toast.makeText(cont, cont.getString(R.string.removido_mensagem), Toast.LENGTH_LONG).show()
                        dialog?.dismiss()
                        recipeList.removeAt(position)
                        notifyItemRemoved(position)
                    }

                })
                .setNegativeButton(cont.getString(R.string.nao), object: DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                        }

                    })
                .setCancelable(true)
                .create()
                dgl.show()
                return true
            }

        })


    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    class ReceitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

class CustomTypefaceSpan(private val typeface: Typeface) : MetricAffectingSpan() {

    override fun updateDrawState(tp: TextPaint) {
        applyCustomTypeFace(tp)
    }

    override fun updateMeasureState(tp: TextPaint) {
        applyCustomTypeFace(tp)
    }

    private fun applyCustomTypeFace(paint: Paint) {
        paint.typeface = typeface
    }
}