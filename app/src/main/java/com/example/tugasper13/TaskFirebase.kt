package com.example.tugasper13

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

// objek tunggal untuk mengelola komunikasi dengan Firebase Firestore
object TaskFirebase {

    // Referensi ke koleksi task di Firestore
    val taskCollectionRef = FirebaseFirestore.getInstance().collection("tasks")
    // MutableLiveData untuk daftar task yang akan diobservasi oleh activity
    val taskListLiveData : MutableLiveData<List<Task>> by lazy {
        MutableLiveData<List<Task>>()
    }

    // Metode untuk mendapatkan semua task dan memulai observe
    fun getAllTasks() {
        observeTasksChange()
    }

    // Mengamati adanya perubahan pada koleksi task di Firestore
    fun observeTasksChange() {
        taskCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d(
                    "MainActivity",
                    "Error Listening for task changes:", error
                )
            }
            val tasks = snapshots?.toObjects(Task::class.java)
            if (tasks != null) {
                taskListLiveData.postValue(tasks)
            }
        }
    }

    // Menangani penghapusan task
    fun deleteTask(task: Task) {
        // Menghapus dokumen task dengan id yang sesuai dari koleksi "tasks"
        taskCollectionRef.document(task.id)
            .delete()
            .addOnSuccessListener {
                Log.d("Delete", "Task deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Delete", "Error deleting task", e)
            }
    }
}