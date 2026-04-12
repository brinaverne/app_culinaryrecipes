package com.example.livrodereceita

import android.util.Log
import com.example.livrodereceita.model.Content
import com.example.livrodereceita.model.GenericResponse
import com.example.livrodereceita.model.Part
import com.example.livrodereceita.model.ReceitaRequest
import com.example.livrodereceita.model.ReceitaResponse
import com.google.gson.Gson

class Remote {

    val apiBaseUrl = "https://generativelanguage.googleapis.com/v1beta"

    fun searchReceitaViaGemini(receita: String): GenericResponse<ReceitaResponse>? {
        var response: GenericResponse<ReceitaResponse>? = GenericResponse<ReceitaResponse>()
        var url = "$apiBaseUrl/models/gemini-3-flash-preview:generateContent"
        var header = mutableMapOf<String, String>()

        var receitaGemini = "Me informe uma receita de "+ receita +" separando o texto nas sessões exatamente com os seguintes nomes:  Título, Ingredientes, Passo a passo"


        var receitaBodyRequest = ReceitaRequest()
        receitaBodyRequest.contents = mutableListOf()
        receitaBodyRequest.contents!!.add(Content().apply {
            parts = mutableListOf<Part>()
            parts!!.add(Part().apply {
                text = receitaGemini }
            )
        })

        try {
            header.put(HandleGemini.geminiKey, HandleGemini.geminiValue)
            val apiResponse = HttpHelper().geminiReceitaPostRequest(url, header, Gson().toJson(receitaBodyRequest))
            response?.objeto = apiResponse
            response?.success = true

        }catch (e:Exception){
            response?.success = false
            response?.exception = e
            Log.e("Erro_no_request", e.message, e)
        }

        return response

    }

}