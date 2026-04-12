package com.example.livrodereceita

import android.util.Log
import com.example.livrodereceita.model.ReceitaResponse
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit

class HttpHelper {

    fun geminiReceitaPostRequest(url: String, headers: Map<String, String>?, jsonBody: String): ReceitaResponse {
        val client = OkHttpClient.Builder()
        val requestBuilder = Request.Builder()
        client.connectTimeout(60L, TimeUnit.SECONDS)
        client.readTimeout(60L, TimeUnit.SECONDS)
        client.callTimeout(60L, TimeUnit.SECONDS)

        headers?.let {
            headers.forEach { header ->
                requestBuilder.addHeader(header.key, header.value)
            }
            requestBuilder.addHeader("Content-Type", "application/json")
        }

        val body: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody)

        val request =
            requestBuilder
                .url(url)
                .post(body)
                .build()

        val response = client.build().newCall(request).execute()

        if(response.code() == 429){
            throw Exception("Limite de Requisições ao Gemini Excedidos")
        }
        val retorno = response.body()!!.string()

        Log.w("REQUEST-POST", String.format("POST-RETORNO: %s", retorno))
        var retornoObjeto = Gson().fromJson<ReceitaResponse>(retorno, ReceitaResponse::class.java)

        return retornoObjeto


    }

}