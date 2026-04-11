package com.example.livrodereceita.repository

import android.content.Context
import com.example.livrodereceita.Remote
import com.example.livrodereceita.model.GenericResponse
import com.example.livrodereceita.model.Receita
import com.example.livrodereceita.model.ReceitaResponse

class ReceitaRepository(context: Context) {

    private val receitaDataBase = ReceitaDataBase.getDataBase(context).receitaDAO()

    fun insert(receita:Receita): Long{
        return receitaDataBase.insert(receita)
    }

    fun update(receita: Receita){
        receitaDataBase.update(receita)
    }

    fun select(): MutableList<Receita>{
        return receitaDataBase.select()

    }

    fun selectItemForUpdate(id: Long): Receita{
        return receitaDataBase.selectItemForUpdate(id)
    }

    fun delete(id: Long){
        receitaDataBase.delete(id)
    }

    fun selectByName(titulo: String): MutableList<Receita>{
        return receitaDataBase.selectByName(titulo)
    }

    fun searchReceitaViaGemini(nomeReceita: String): GenericResponse<ReceitaResponse>?{
        return Remote().searchReceitaViaGemini(nomeReceita)
    }

}