package com.rkasibhatla.roomdbdemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Insert
    suspend fun insert(student: Student)

    @Query("select * from student")
    fun getAllStudents(): Flow<List<Student>>

    @Query("delete from student")
    suspend fun deleteAllRecords()

    @Transaction
    @Query("select * from student where studentId=:studentId")
    fun getElectives(studentId: Int): StudentWithElectives

    @Query("select * from student where name=:name")
    fun getStudentByName(name: String): Student
}