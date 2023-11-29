package com.example.tugasper13

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DeleteTaskFragment(private val task: Task) : DialogFragment() {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Set the message for the dialog
            builder.setMessage("Ingin menghapus tugas ini?")
                // Tombol menghapus task
                .setPositiveButton("Ya") { dialog, id ->
                    deleteTask()
                    // Kembali ke MainActivity setelah berhasil menghapus
                    (activity as? DetailTaskActivity)?.finish()

                }
                // Tombol membatalkan penghapusan
                .setNegativeButton("Batal") { dialog, id ->
                    // Menutup dialog
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Menjalankan penghapusan
    private fun deleteTask() {
        // Menjalankan penghapusan di latar belakang
        executorService.execute {
            // Memanggil deleteTask pada TaskFirebase
            TaskFirebase.deleteTask(task)
        }
    }
}
