package com.rkasibhatla.roomdbdemo

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val studentViewModel: StudentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addStudentButton = findViewById<Button>(R.id.btn_add_student)
        addStudentButton.setOnClickListener {
            val stName = findViewById<EditText>(R.id.ed_student_name).text.toString()
            val deptName = findViewById<EditText>(R.id.ed_dept_name).text.toString()
            if(!deptName.isEmpty()) {
                GlobalScope.launch {
                    val deptId = studentViewModel.getDepartmentByName(deptName).deptId
                    val student = Student(name = stName, deptId = deptId)
                    println("Adding student: $stName dept id: $deptId")
                    studentViewModel.insertStudent(student)
                }
            } else {
                Toast.makeText(this, "Department name is empty", Toast.LENGTH_SHORT).show()
            }
        }

        val addDeptButton = findViewById<Button>(R.id.btn_add_dept)
        addDeptButton.setOnClickListener {
            val deptName = findViewById<EditText>(R.id.ed_dept_name).text.toString()
            GlobalScope.launch {
                val dept = Department(name = deptName)
                studentViewModel.insertDepartment(dept)
            }

        }

        val addElectiveButton = findViewById<Button>(R.id.btn_add_elective)
        addElectiveButton.setOnClickListener {
            val elName = findViewById<EditText>(R.id.ed_elective_name).text.toString()
            GlobalScope.launch {
                val elective = Elective(electiveName = elName)
                studentViewModel.insertElective(elective)
            }
        }

        val resetBtn = findViewById<Button>(R.id.btn_reset)
        resetBtn.setOnClickListener {
            GlobalScope.launch {
                studentViewModel.deleteAllRecords()
            }
        }

        val btnAddStudentElective = findViewById<Button>(R.id.btn_add_student_elective)
        btnAddStudentElective.setOnClickListener {
            GlobalScope.launch {
                val stName = findViewById<EditText>(R.id.ed_student_name).text.toString()
                val stId = studentViewModel.getStudentByName(stName).studentId
                val elName = findViewById<EditText>(R.id.ed_elective_name).text.toString()
                val elId = studentViewModel.getElectiveByName(elName).electiveId
                println("Got student id: $stId elective id: $elId")
                val studentElectiveCrossRef = StudentElectiveCrossRef(stId,elId)
                studentViewModel.insertStudentElectiveCrossRef(studentElectiveCrossRef)
                println("Added student id: $stId elective id: $elId")
            }
        }

        val btnGetStudent = findViewById<Button>(R.id.btn_get_student)
        btnGetStudent.setOnClickListener {
            val stName = findViewById<EditText>(R.id.ed_student_name).text.toString()
            if(stName.isNotEmpty()) {
                GlobalScope.launch {
                    val student = studentViewModel.getStudentByName(stName)
                    val stId = student.studentId
                    val deptId = student.deptId
                    println("Student $stName with id: $stId in dept id: $deptId")
                    val studentElectives = studentViewModel.getStudentElectives(stId)
                    for(e:Elective in studentElectives.electives)
                        println("Student $stName with id: $stId has elective ${e.electiveName}")
                }
            }
        }

        val btnGetDepartment = findViewById<Button>(R.id.btn_get_department)
        btnGetDepartment.setOnClickListener {
            val deptName = findViewById<EditText>(R.id.ed_dept_name).text.toString()
            if(deptName.isNotEmpty()) {
                GlobalScope.launch {
                    val dept = studentViewModel.getDepartmentByName(deptName)
                    println("Department $deptName with id: ${dept.deptId}")
                }
            }
        }

        val btnGetElective = findViewById<Button>(R.id.btn_get_elective)
        btnGetElective.setOnClickListener {
            val elName = findViewById<EditText>(R.id.ed_elective_name).text.toString()
            if(elName.isNotEmpty()) {
                GlobalScope.launch {
                    val elective = studentViewModel.getElectiveByName(elName)
                    println("Elective $elName with id: ${elective.electiveId}")
                    val electiveStudents = studentViewModel.getElectiveStudents(elName)
                    for(electiveStudent: Student in electiveStudents.students)
                        println("Student ${electiveStudent.name} with id: ${electiveStudent.studentId} has taken elective $elName")
                }
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerViewAdapter = StudentRecyclerAdapter(studentViewModel)
        recyclerView.adapter = recyclerViewAdapter
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        studentViewModel.allStudents.observe(this, {
            recyclerViewAdapter.notifyDataSetChanged()
        })
    }
}

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val db: StudentDatabase = StudentDatabase(application)
    var allStudents: LiveData<List<Student>> = db.studentDao().getAllStudents().asLiveData()
    var departmentWithStudents: LiveData<DepartmentWithStudents> =
        db.departmentDao().getDepartmentsWithStudents(1).asLiveData()

    suspend fun insertStudent(student: Student) {
        viewModelScope.launch {
            db.studentDao().insert(student)
        }
    }

    fun getDepartmentByName(name: String): Department {
        return db.departmentDao().getDepartmentByName(name)
    }

    suspend fun insertDepartment(department: Department) {
        viewModelScope.launch {
            db.departmentDao().insert(department)
        }
    }

    fun getAllDepartments(): List<Department> {
        return db.departmentDao().getAllDepartments()
    }

    suspend fun insertElective(elective: Elective) {
        viewModelScope.launch {
            db.electiveDao().insert(elective)
        }
    }

    fun getAllElectives(): List<Elective> {
        return db.electiveDao().getAllElectives()
    }

    suspend fun insertStudentElectiveCrossRef(studentElectiveCrossRef: StudentElectiveCrossRef) {
        viewModelScope.launch {
            db.studentElectiveCrossRefDao().insert(studentElectiveCrossRef)
        }
    }

    fun getStudentElectives(studentId: Int): StudentWithElectives {
        return db.studentDao().getElectives(studentId)
    }

    fun getElectiveStudents(name: String): ElectiveWithStudents {
        return db.electiveDao().getStudents(name)
    }

    suspend fun deleteAllRecords() {
        viewModelScope.launch {
            db.electiveDao().deleteAllRecords()
            db.departmentDao().deleteAllRecords()
            db.studentElectiveCrossRefDao().deleteAllRecords()
            db.studentDao().deleteAllRecords()
        }
    }

    fun getStudentByName(name: String): Student {
        return db.studentDao().getStudentByName(name)
    }

    fun getElectiveByName(name: String): Elective {
        return db.electiveDao().getElectiveByName(name)
    }

    fun getDepartmentById(deptId: Int): Department {
        return db.departmentDao().getDepartmentById(deptId)
    }
}