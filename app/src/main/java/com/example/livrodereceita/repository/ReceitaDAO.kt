package com.example.livrodereceita.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.livrodereceita.model.Receita

@Dao
interface ReceitaDAO {

    @Insert
    fun insert(receita:Receita): Long

    @Update
    fun update(receita:Receita)

    @Query("SELECT * FROM Receita")
    fun select(): MutableList<Receita>

    @Query("SELECT * FROM Receita WHERE id = :id")
    fun selectItemForUpdate(id: Long): Receita

    @Query("DELETE FROM Receita WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM Receita WHERE titulo like :titulo")
    fun selectByName(titulo: String): MutableList<Receita>

}