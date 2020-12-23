package com.rkasibhatla.roomdbdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Department(
    @PrimaryKey(autoGenerate = true)
    var deptId: Int = 0,
    @ColumnInfo(name="dept_name")
    val name: String
)