package com.rkasibhatla.roomdbdemo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentElectiveCrossRefDao {

    @Insert
    suspend fun insert(studentElective: StudentElectiveCrossRef)

    @Query("delete from studentelectivecrossref")
    suspend fun deleteAllRecords()
}