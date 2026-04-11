package com.example.livrodereceita.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.livrodereceita.model.Receita

@Database(entities = [Receita::class], version = 1)
abstract class ReceitaDataBase(): RoomDatabase() {

    abstract fun receitaDAO(): ReceitaDAO

    companion object{

        private lateinit var instance: ReceitaDataBase

        fun getDataBase(context: Context): ReceitaDataBase {

            if(!Companion::instance.isInitialized){
                synchronized(ReceitaDataBase::class){
                    instance = Room.databaseBuilder(context, ReceitaDataBase::class.java, "receitadb")
                        .allowMainThreadQueries()
                        .build()
                }

            }
            return instance
        }


    }

}