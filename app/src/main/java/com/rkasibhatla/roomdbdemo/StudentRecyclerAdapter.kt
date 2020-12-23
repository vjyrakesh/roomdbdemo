package com.rkasibhatla.roomdbdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudentRecyclerAdapter(private val studentModel: StudentViewModel):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val studentNameView: TextView = itemView.findViewById(R.id.tv_student_name)
        val deptNameView: TextView = itemView.findViewById(R.id.tv_dept_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val deptWithStudent = studentModel.allStudents.value?.get(position)
        (holder as StudentViewHolder).studentNameView.text =
            "${deptWithStudent?.name}"
        GlobalScope.launch {
            val deptName = studentModel.getDepartmentById(deptWithStudent?.deptId?:0).name
            holder.deptNameView.text = deptName
        }

    }

    override fun getItemCount(): Int {
        return studentModel.allStudents.value?.size?:0
    }
}