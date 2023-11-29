package com.example.tugasper13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasper13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            // Klik tombol Tambah
            btnTambah.setOnClickListener {
                startActivity(
                    Intent(this@MainActivity, FormTaskActivity::class.java)
                )
            }
        }

        // Memanggil metode untuk mengamati perubahan pada daftar task
        observeTasks()
        // Memanggil metode untuk mengambil semua data task dari Firebase Firestore
        TaskFirebase.getAllTasks()
    }

    // Mengamati perubahan pada daftar task
    private fun observeTasks() {
        TaskFirebase.taskListLiveData.observe(this) { tasks ->
            // Membuat adapter dengan data terbaru
            val adapterTask = TaskAdapter(tasks) { task ->
                //Membuka DetailTaskActivity saat item task diklik
                startActivity(
                    Intent(this@MainActivity, DetailTaskActivity::class.java).putExtra("task", task)
                )
            }

            // Menerapkan adapter ke RecyclerView
            binding.rvTask.apply {
                adapter = adapterTask
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }
}

