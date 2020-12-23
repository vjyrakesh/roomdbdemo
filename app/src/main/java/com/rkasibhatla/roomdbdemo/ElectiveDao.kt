package com.rkasibhatla.roomdbdemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ElectiveDao {

    @Insert
    suspend fun insert(elective: Elective)

    @Query("select * from elective where elective_name=:name")
    fun getStudents(name: String): ElectiveWithStudents

    @Query("select * from elective")
    fun getAllElectives(): List<Elective>

    @Query("delete from elective")
    suspend fun deleteAllRecords()

    @Query("select * from elective where elective_name=:name")
    fun getElectiveByName(name: String): Elective
}