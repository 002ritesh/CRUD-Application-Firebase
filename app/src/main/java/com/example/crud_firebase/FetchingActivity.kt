package com.example.crud_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_firebase.adapter.EmployeeAdapter
import com.example.crud_firebase.databinding.ActivityFetchingBinding
import com.example.crud_firebase.model.Employee
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class FetchingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFetchingBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var empList: ArrayList<Employee>
//    private lateinit var empAdapter: Adapter

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFetchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

       recyclerView = binding.recyclerViewId
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        empList = arrayListOf()

        getEmpData()
    }

    private fun getEmpData() {
        recyclerView.visibility = View.GONE
        binding.tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(Employee::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmployeeAdapter(empList)
                    recyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : EmployeeAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)
                            //put extra
                            intent.putExtra("empId", empList[position].empId)
                            intent.putExtra("empName", empList[position].empName)
                            intent.putExtra("empAge", empList[position].empEge)
                            intent.putExtra("empSalary", empList[position].empSalary)
                            startActivity(intent)
                        }

                    })

                    recyclerView.visibility = View.VISIBLE
                    binding.tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}