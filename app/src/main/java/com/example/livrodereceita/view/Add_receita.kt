package com.example.livrodereceita.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import com.example.livrodereceita.Cache
import com.example.livrodereceita.R
import com.example.livrodereceita.model.Receita
import com.example.livrodereceita.viewmodel.ReceitaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Add_receita : AppCompatActivity() {

    lateinit var viewModel: ReceitaViewModel
    lateinit var receita: Receita
    lateinit var titulo: EditText
    lateinit var autor: EditText
    lateinit var ingredientes: EditText
    lateinit var passos: EditText
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_receita)

        viewModel = ViewModelProvider(this).get(ReceitaViewModel::class.java)

        val id = intent.getLongExtra("id", 0)
        val isSearching = intent.getBooleanExtra("isSearching", false)

        var btncad: Button = findViewById(R.id.btntlcadastro)
        titulo = findViewById(R.id.titulo)
        autor = findViewById(R.id.autor)
        ingredientes = findViewById(R.id.ingredientes)
        passos = findViewById(R.id.passos)
        var titulotela:TextView = findViewById(R.id.activitytitle)
        var btnalt: FloatingActionButton = findViewById(R.id.btnalt)
        var btnPequisar: Button = findViewById(R.id.btn_pesquisar_receita)
        var inputTextPesquisarReceita: TextInputEditText = findViewById(R.id.search_recipe)
        var PaiInputText: TextInputLayout = findViewById(R.id.text_input_layout_pesquisar_receita)

        btnalt.isInvisible = true


        viewModel.receita.observe(this, {itemreceita ->
            receita = itemreceita
            autor.setText(itemreceita.autor)
            titulo.setText(itemreceita.titulo)
            passos.setText(itemreceita.passos)
            ingredientes.setText(itemreceita.ingredientes)
            blockCampos(false)
            btncad.isInvisible = true
            titulotela.text = getString(R.string.receita)
            btnalt.isInvisible = false
            btncad.text = getString(R.string.gravar_receita)


        })

        viewModel.receitaGeminiMutableLiveData.observe(this,{
            if(it?.success == false){
                if (it.exception != null){
                    Toast.makeText(this, it?.exception?.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.falha_pesquisa), Toast.LENGTH_SHORT).show()
                }

            } else{
                autor.setText(getString(R.string.gemini))
                titulo.setText(it?.objeto?.titulo)
                passos.setText(it?.objeto?.passos)
                ingredientes.setText(it?.objeto?.ingredientes)
                btncad.isInvisible = false
            }

            alertDialogLoaging(this@Add_receita, false)

        })

        if (id > 0) viewModel.selectItemForUpdate(id)


        btnalt.setOnClickListener {
            btncad.isInvisible = false
            blockCampos(true)
            btnalt.isInvisible = true
            titulotela.text = getString(R.string.alterar_receita)
        }


        btncad.setOnClickListener {
            var toastmensagem: String
            var idNovaReceita: Long
            if (titulo.text.isNullOrEmpty() || autor.text.isNullOrEmpty() || ingredientes.text.isNullOrEmpty() || passos.text.isNullOrEmpty()){
                Toast.makeText(this@Add_receita, getString(R.string.campos_vazios), Toast.LENGTH_LONG).show()
            }
            else{
                if(!::receita.isInitialized){
                    receita = Receita()
                    receita.titulo = titulo.text.toString()
                    receita.autor = autor.text.toString()
                    receita.ingredientes = ingredientes.text.toString()
                    receita.passos = passos.text.toString()
                    idNovaReceita = viewModel.insert(receita)
                    toastmensagem = getString(R.string.cadastrado_com_sucesso)
                    viewModel.selectItemForUpdate(idNovaReceita)
                    PaiInputText.visibility = View.GONE
                    btnPequisar.visibility = View.GONE

                } else{
                    receita.titulo = titulo.text.toString()
                    receita.autor = autor.text.toString()
                    receita.ingredientes = ingredientes.text.toString()
                    receita.passos = passos.text.toString()
                    viewModel.update(receita)
                    toastmensagem = getString(R.string.alterado_com_sucesso)
                }

                Toast.makeText(this@Add_receita, toastmensagem, Toast.LENGTH_SHORT).show()
            }
        }

        if(isSearching){
            btncad.isInvisible = true
            btnalt.isInvisible = true
            blockCampos(false)
            titulotela.text = getString(R.string.pesquise_sua_receita)

            PaiInputText.visibility = View.VISIBLE
            btnPequisar.visibility = View.VISIBLE
        }

        btnPequisar.setOnClickListener {
            viewModel.searchReceitaViaGemini(inputTextPesquisarReceita.text.toString())
            alertDialogLoaging(this@Add_receita, true)
        }


    }

    fun blockCampos(value: Boolean){
        titulo.isEnabled = value
        autor.isEnabled = value
        ingredientes.isEnabled = value
        passos.isEnabled = value
    }

    fun alertDialogLoaging(context:Context, show:Boolean){

        if(dialog == null){
            val dialogView = layoutInflater.inflate(R.layout.alert_dialog_loading, null)
            dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create()


        }
        if(show){
            dialog?.show()
        } else{
            dialog?.dismiss()
        }

    }


}