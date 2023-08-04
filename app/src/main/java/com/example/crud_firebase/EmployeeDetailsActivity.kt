package com.example.crud_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.crud_firebase.databinding.ActivityEmployeeDetailsBinding
import com.example.crud_firebase.model.Employee
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setValuesToView()

        binding.updateBtn.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                        intent.getStringExtra("empName").toString()
            )
        }

    }

    private fun openUpdateDialog(empId: String, empName: String) {

        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etEmployeeName = mDialogView.findViewById<EditText>(R.id.editName)
        val etEmployeeAge = mDialogView.findViewById<EditText>(R.id.editAge)
        val etEmployeeSalary = mDialogView.findViewById<EditText>(R.id.editSalary)

        val updateDataBtn = mDialogView.findViewById<EditText>(R.id.updataDataBtn)

        etEmployeeName.setText(intent.getStringExtra("empName").toString())
        etEmployeeAge.setText(intent.getStringExtra("empAge").toString())
        etEmployeeSalary.setText(intent.getStringExtra("empSalary").toString())

        mDialog.setTitle("Updating ${empName} Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        updateDataBtn.setOnClickListener {
            updateEmpData(
                empId,
                etEmployeeName.text.toString(),
                etEmployeeAge.text.toString(),
                etEmployeeSalary.text.toString()
            )
            Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_SHORT).show()

            //we are setting updated data to our textView
            binding.tvName.text = etEmployeeName.text.toString()
            binding.tvAge.text = etEmployeeAge.text.toString()
            binding.tvSalary.text = etEmployeeSalary.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val empInfo = Employee(id, name, age, salary)
        dbRef.setValue(empInfo)

    }

    private fun initView() {

    }

    private fun setValuesToView(){
        binding.tvId.text = intent.getStringExtra("empId")
        binding.tvName.text = intent.getStringExtra("empName")
        binding.tvAge.text = intent.getStringExtra("empAge")
        binding.tvSalary.text = intent.getStringExtra("empSalary")
    }
}