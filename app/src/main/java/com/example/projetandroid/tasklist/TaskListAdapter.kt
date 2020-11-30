package com.example.projetandroid.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projetandroid.R

class TaskListAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {
    private var myTaskList = taskList

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(myTask : Task) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                // TODO: afficher les données et attacher les listeners aux différentes vues de notre [itemView]
                var task = findViewById<TextView>(R.id.task_title)
                var desc = findViewById<TextView>(R.id.task_desc)
                var deleteButton = findViewById<ImageButton>(R.id.delete_button)
                var modifyButton = findViewById<ImageButton>(R.id.modify_button)

                task.text = myTask.title
                desc.text = myTask.description
                deleteButton.setOnClickListener {
                    onDeleteClickListener?.invoke(myTask)
                }
                modifyButton.setOnClickListener {
                    onModifyClickListener?.invoke(myTask)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(myTaskList[position])

    }

    override fun getItemCount(): Int {
        return myTaskList.size;

    }

    var onDeleteClickListener: ((Task) -> Unit)? = null
    var onModifyClickListener: ((Task) -> Unit)? = null
}