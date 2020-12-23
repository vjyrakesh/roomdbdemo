package com.rkasibhatla.roomdbdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Elective(
    @PrimaryKey(autoGenerate = true)
    val electiveId: Int=0,
    @ColumnInfo(name = "elective_name")
    val electiveName: String
)