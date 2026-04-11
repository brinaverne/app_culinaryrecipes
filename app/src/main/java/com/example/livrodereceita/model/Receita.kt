package com.example.livrodereceita.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Receita")
class Receita {

    @ColumnInfo(name = "titulo")
    var titulo: String = ""

    @ColumnInfo(name = "autor")
    var autor: String = ""

    @ColumnInfo(name = "passos")
    var passos: String = ""

    @ColumnInfo(name = "ingredientes")
    var ingredientes: String = ""

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}