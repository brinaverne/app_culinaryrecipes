package com.example.livrodereceita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.livrodereceita.model.GenericResponse
import com.example.livrodereceita.model.Receita
import com.example.livrodereceita.repository.ReceitaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ReceitaViewModel(application: Application): AndroidViewModel(application) {

    private var repository = ReceitaRepository(application.applicationContext)
    var receitaGeminiMutableLiveData = MutableLiveData<GenericResponse<Receita?>?>()

    var listAllReceita = MutableLiveData<List<Receita>>()
    var receita = MutableLiveData<Receita>()

    fun insert(receita:Receita): Long{
        return repository.insert(receita)
    }

    fun update(receita:Receita){
        repository.update(receita)
    }

    fun select(){
        listAllReceita.postValue(repository.select())
    }

    fun selectItemForUpdate(id: Long){
        receita.postValue(repository.selectItemForUpdate(id))
    }

    fun delete(id: Long){
        repository.delete(id)
    }

    fun selectByName(titulo: String){
        listAllReceita.postValue(repository.selectByName(titulo))
    }

    fun searchReceitaViaGemini(nomeReceita: String) = GlobalScope.launch(Dispatchers.IO){
        var retorno = repository.searchReceitaViaGemini(nomeReceita)
        var genericResponse = GenericResponse<Receita?>()

        if(retorno?.success == true){
            var receitaText = retorno?.objeto?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            var receita = Receita()
            if(!receitaText.isNullOrEmpty()){
                receita.titulo = receitaText.split("Título")[1].split("Ingredientes")[0].replace("*", "").replace("\n", "")
                receita.ingredientes = receitaText.split("Título")[1].split("Ingredientes")[1].split("Passo a passo")[0].replace("**", "").replace("\n", "").replace("*", "\n")
                receita.passos = receitaText.split("Título")[1].split("Ingredientes")[1].split("Passo a passo")[1].replace("**", "")
                genericResponse.objeto = receita
                genericResponse.success = true
                receitaGeminiMutableLiveData.postValue(genericResponse)
            } else{
                genericResponse.objeto = null
                genericResponse.success = false
                receitaGeminiMutableLiveData.postValue(genericResponse)
            }
        } else{
            receitaGeminiMutableLiveData.postValue(retorno as GenericResponse<Receita?>)
        }



    }

}