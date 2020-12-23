package com.rkasibhatla.roomdbdemo

import androidx.room.Entity

@Entity(primaryKeys = arrayOf("studentId", "electiveId"))
data class StudentElectiveCrossRef(
    val studentId: Int,
    val electiveId: Int
)