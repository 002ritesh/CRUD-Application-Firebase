package com.example.crud_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.crud_firebase.databinding.ActivityInsertBinding
import com.example.crud_firebase.model.Employee
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.System.err

class InsertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        binding.savebtn.setOnClickListener {
            saveEmployeeData()
        }

    }

    private fun saveEmployeeData() {

        //getting values
        val empName = binding.name.text.toString()
        val empAge = binding.age.text.toString()
        val empSalary = binding.salary.text.toString()

        if (empName.isEmpty()) {
            binding.name.error = "Please Enter Your Name"
        }
        if (empAge.isEmpty()) {
            binding.age.error = "Please Enter Your Age"
        }
        if (empSalary.isEmpty()) {
            binding.salary.error = "Please Enter Your Salary"
        }

        val empId = dbRef.push().key!!

        val emp = Employee(empId, empName, empAge, empSalary)

        dbRef.child(empId).setValue(emp).addOnCompleteListener {
            Toast.makeText(this, "Data is inserted Successfully", Toast.LENGTH_SHORT).show()

            binding.name.text.clear()
            binding.age.text.clear()
            binding.salary.text.clear()

        }.addOnFailureListener { err ->
            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
        }

    }
}