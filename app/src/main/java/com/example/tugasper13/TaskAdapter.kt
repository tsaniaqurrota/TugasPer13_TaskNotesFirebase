package com.example.tugasper13

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasper13.databinding.ItemTaskBinding

typealias OnClickTask = (Task) ->Unit

class TaskAdapter (
    private var listsTask: List<Task>,
    private val onClickTask: OnClickTask):

    RecyclerView.Adapter<TaskAdapter.ItemTaskViewHolder>() {

    // Memperbarui data task dengan memberi tahu adapter
    fun updateData(newTasks: List<Task>): Int {
        listsTask = newTasks
        notifyDataSetChanged()
        return listsTask.size
    }

    inner class ItemTaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Mengambil data task ke tampilan
        fun bind(task : Task) {
            with(binding) {
                titleTxt.text = task.title
                descTxt.text = task.description
                dosenTxt.text = task.dosen
                deadlineTxt.text = task.deadline

                // Saat item task diklik
                itemView.setOnClickListener {
                    onClickTask(task)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listsTask.size
    }

    override fun onBindViewHolder(holder: ItemTaskViewHolder, position: Int) {
        holder.bind(listsTask[position])
    }
}



