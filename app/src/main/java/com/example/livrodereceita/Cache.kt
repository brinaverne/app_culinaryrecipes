package com.example.livrodereceita

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Cache {
    private val PREF_ID = "receita_cache"

    fun setReceita(context: Context, listaCache: ArrayList<Receita>){
        val editor = context.getSharedPreferences(PREF_ID, 0).edit()

        editor.putString("lista_receita", Gson().toJson(listaCache))
        editor.apply()

    }

    fun getReceita(context: Context):ArrayList<Receita>{
        val editor = context.getSharedPreferences(PREF_ID, 0)
        val getlista = editor.getString("lista_receita", "")
        val type = object : TypeToken<ArrayList<Receita>>(){}.type

        return if(getlista.isNullOrEmpty()) arrayListOf<Receita>() else Gson().fromJson<ArrayList<Receita>>(getlista, type)
    }

    fun countid(context: Context):Long{
        val lista = getReceita(context)
        return (lista.size + 1).toLong()
    }

    fun id(context: Context, iditem:Long):Receita?{
        val lista = getReceita(context)
        return lista.firstOrNull{
            it.id == iditem
        }
    }

}