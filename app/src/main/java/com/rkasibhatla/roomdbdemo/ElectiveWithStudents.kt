package com.rkasibhatla.roomdbdemo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ElectiveWithStudents(
    @Embedded
    val elective: Elective,

    @Relation(
        parentColumn = "electiveId",
        entityColumn = "studentId",
        associateBy = Junction(StudentElectiveCrossRef::class)
    )
    val students: List<Student>
)