package com.rkasibhatla.roomdbdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Student::class, Department::class, Elective::class, StudentElectiveCrossRef::class), version = 3, exportSchema = false)
abstract class StudentDatabase: RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun electiveDao(): ElectiveDao
    abstract fun studentElectiveCrossRefDao(): StudentElectiveCrossRefDao

    companion object {
        @Volatile private var INSTANCE: StudentDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE?: synchronized(LOCK) {
            INSTANCE?: buildDatabase(context).also{ INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            StudentDatabase::class.java, "studentdb").fallbackToDestructiveMigration().build()
    }
}