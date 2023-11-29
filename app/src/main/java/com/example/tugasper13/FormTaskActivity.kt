package com.example.tugasper13

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tugasper13.databinding.ActivityFormTaskBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FormTaskActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityFormTaskBinding.inflate(layoutInflater)
    }

    // Menyimpan extra pada intent dengan companion object
    companion object {
        const val EXTRA_TASK = "extra_task"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {

            // Cek apakah sudah terdapat ekstra task dalam intent
            if (intent.hasExtra("task")) {
                // Jika sudah, akan menjadi pengeditan task
                val task = intent.getSerializableExtra("task") as Task
                // Mengisi formulir dengan data task yang ada
                edtTitleTask.setText(task.title)
                edtDescTask.setText(task.description)
                edtDeadlineTask.setText(task.deadline)
                edtDosen.setText(task.dosen)

                btnSimpan.setOnClickListener {
                    //Membuat object Task yg telah diubah
                    val editedTask = Task(
                        id = task.id,
                        title = edtTitleTask.text.toString(),
                        description = edtDescTask.text.toString(),
                        deadline = edtDeadlineTask.text.toString(),
                        dosen = edtDosen.text.toString()
                    )
                    // Memastikan id task tidak kosong sebelum memperbarui task
                    if (task.id.isNotEmpty()) {
                        // Memperbarui task di database
                        updateTask(editedTask)
                        //Mengirim kembali data ke MainActivity
                        val intent = Intent()
                        intent.putExtra(EXTRA_TASK, editedTask)
                        setResult(Activity.RESULT_OK, intent)
                        //Menutup activity FormTaskActivity
                        finish()
                    }
                }
            } else {
                //Jika belum ada ekstra task, akan dibuat task baru
                btnSimpan.setOnClickListener {
                    //Cek apakah inputan telah diisi
                    if (edtTitleTask.text.toString() != "" && edtDescTask.text.toString() != "" && edtDeadlineTask.text.toString() != "" && edtDosen.text.toString() != "") {
                        // Jika telah terisi, object Task baru akan dibuat
                        val newTask = Task(
                            title = edtTitleTask.text.toString(),
                            description = edtDescTask.text.toString(),
                            deadline = edtDeadlineTask.text.toString(),
                            dosen = edtDosen.text.toString()
                        )
                        // Menyimpan task baru ke database
                        addTask(newTask)
                    } else {
                        // Jika ada input yang kosong, akan ditampilkan pesan peringatan
                        Toast.makeText(
                            this@FormTaskActivity,
                            "Kolom tidak boleh kosong.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    // Menambahkan task baru ke Firestore
    private fun addTask(task: Task) {
        TaskFirebase.taskCollectionRef.add(task).addOnSuccessListener { documentReference ->
            // Mendapatkan id yang dibuat oleh Firestore
            val createdTaskId = documentReference.id
            task.id = createdTaskId
            // Menyimpan kembali id ke dokumen task
            documentReference.set(task).addOnSuccessListener {
                // Membuka activity MainActivity
                startActivity(
                    Intent(this@FormTaskActivity, MainActivity::class.java)
                        .putExtra("task", task)
                )
                // Menutup activity FormTaskActivity
                finish()
            }.addOnFailureListener {
                Log.d("TaskFormActivity", "Error updating task id: ", it)
            }
        }.addOnFailureListener {
            Log.d("TaskFormActivity", "Error adding task id: ", it)
        }
    }

    // Memperbarui task yang ada di Firestore
    private fun updateTask(task: Task) {
        TaskFirebase.taskCollectionRef.document(task.id).set(task).addOnFailureListener {
            Log.d("FormTaskActivity", "Error updating task", it)
        }
    }
}