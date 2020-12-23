package com.rkasibhatla.roomdbdemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {

    @Insert
    suspend fun insert(department: Department)

    @Transaction
    @Query(value = "select * from department where deptId=:deptId")
    fun getDepartmentsWithStudents(deptId:Int): Flow<DepartmentWithStudents>

    @Query(value = "select * from department")
    fun getAllDepartments(): List<Department>

    @Query("delete from department")
    suspend fun deleteAllRecords()

    @Query("select * from department where dept_name=:name")
    fun getDepartmentByName(name: String): Department

    @Query("select * from department where deptId=:deptId")
    fun getDepartmentById(deptId: Int): Department
}