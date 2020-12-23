package com.rkasibhatla.roomdbdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey(autoGenerate = true)
    var studentId: Int = 0,
    @ColumnInfo(name="name")
    var name: String,
    val deptId: Int
)