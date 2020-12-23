package com.rkasibhatla.roomdbdemo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StudentWithElectives(
    @Embedded
    val student: Student,

    @Relation(
        parentColumn = "studentId",
        entityColumn = "electiveId",
        associateBy = Junction(StudentElectiveCrossRef::class)
    )
    val electives: List<Elective>
)