package com.rkasibhatla.roomdbdemo

import androidx.room.Embedded
import androidx.room.Relation


data class DepartmentWithStudents(
    @Embedded
    val dept: Department,
    @Relation(
        parentColumn = "deptId",
        entityColumn = "deptId"
    )
    val students: List<Student>
)