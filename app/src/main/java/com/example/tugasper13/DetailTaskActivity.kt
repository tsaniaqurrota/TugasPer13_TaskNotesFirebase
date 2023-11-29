package com.example.tugasper13

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tugasper13.FormTaskActivity.Companion.EXTRA_TASK
import com.example.tugasper13.databinding.ActivityDetailTaskBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailTaskActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailTaskBinding.inflate(layoutInflater)
    }

    // Variabel untuk menyimpan task
    lateinit var task: Task

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Memperbarui tampilan jika hasil aktivitas FormTaskActivity adalah OK
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            task = data?.getSerializableExtra(EXTRA_TASK) as Task

            with(binding) {
                // Set data task ke tampilan
                detailTugas.setText(task.title)
                detailDeadline.setText(task.deadline)
                detailDosen.setText(task.dosen)
                detailDesc.setText(task.description)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            // Mengambil objek Task dari intent
            task = intent.getSerializableExtra("task") as Task
            // Set data task ke tampilan
            detailTugas.setText(task.title)
            detailDeadline.setText(task.deadline)
            detailDosen.setText(task.dosen)
            detailDesc.setText(task.description)

            // Klik tombol Edit
            btnUpdate.setOnClickListener{
                // Membuat intent untuk FormTaskActivity dengan objek Task yang akan diubah
                val intentToFormTaskActivity = Intent(this@DetailTaskActivity, FormTaskActivity::class.java)
                .putExtra("task", task)
                // Menjalankan aktivitas FormTaskActivity
                launcher.launch(intentToFormTaskActivity)
            }

            //Klik tombol Delete
            btnDelete.setOnClickListener{
                // Menjalankan DeleteTaskFragment dan menampilkan dialognya
                DeleteTaskFragment(task).show(supportFragmentManager,"DELETE_DIALOG")
            }
        }
    }
}
