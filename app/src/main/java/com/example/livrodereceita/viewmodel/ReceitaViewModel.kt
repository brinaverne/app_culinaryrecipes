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
import kotlinx.coroutines.delay
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

    fun searchReceitaViaGeminiMock(nomeReceita: String) = GlobalScope.launch(Dispatchers.IO) {
        var genericResponse = GenericResponse<Receita?>()
        val mockedResponse = "Aqui está a receita conforme solicitado:\n\n**Título**\nBatata Frita Caseira Crocante\n\n**Ingredientes**\n* 500g de batatas (preferencialmente do tipo Asterix, da casca rosada)\n* Óleo vegetal para fritar (o suficiente para cobrir as batatas)\n* Sal a gosto\n\n**Passo a passo**\n1. Descasque as batatas e corte-as em palitos de espessura uniforme.\n2. Coloque as batatas cortadas em uma tigela com água bem gelada por cerca de 15 a 20 minutos para remover o excesso de amido.\n3. Escorra a água e seque os palitos muito bem com um pano de prato limpo ou papel toalha. A batata deve estar bem seca antes de ir para o óleo.\n4. Aqueça o óleo em uma panela funda. Para saber se está no ponto, coloque um palito de batata: se borbulhar intensamente, está pronto.\n5. Frite as batatas em pequenas porções para não esfriar o óleo. Deixe fritar até que fiquem douradas e crocantes.\n6. Retire as batatas com uma escumadeira e coloque-as sobre um prato forrado com papel toalha para absorver o excesso de gordura.\n7. Tempere com sal enquanto as batatas ainda estiverem quentes e sirva em seguida."
        
        var receita = Receita()

        delay(5000)

        if(!mockedResponse.isNullOrEmpty()){
            receita.titulo = mockedResponse.split("Título")[1].split("Ingredientes")[0].replace("*", "").replace("\n", "")
            receita.ingredientes = mockedResponse.split("Título")[1].split("Ingredientes")[1].split("Passo a passo")[0].replace("**", "").replace("\n", "").replace("*", "\n")
            receita.passos = mockedResponse.split("Título")[1].split("Ingredientes")[1].split("Passo a passo")[1].replace("**", "")
            genericResponse.objeto = receita
            genericResponse.success = true
            receitaGeminiMutableLiveData.postValue(genericResponse)
        } else{
            genericResponse.objeto = null
            genericResponse.success = false
            receitaGeminiMutableLiveData.postValue(genericResponse)
        }
    }

}