package com.example.projetandroid.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetandroid.R
import com.example.projetandroid.tasklist.task.TaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TaskListFragment : Fragment() {
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
    }

    var myAdapter : TaskListAdapter? = null

    private var taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Pour une [RecyclerView] ayant l'id "recycler_view":
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        myAdapter = TaskListAdapter(taskList)
        recyclerView.adapter = myAdapter
        var button = view.findViewById<FloatingActionButton>(R.id.addButton)

        button.setOnClickListener {
            //addTask()
            //myAdapter.notifyItemChanged(taskList.size)
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        myAdapter!!.onDeleteClickListener = { task ->
            val position = taskList.indexOf(task)
            taskList.remove(task)
            myAdapter!!.notifyItemRemoved(position)
        }
    }

    private fun addTask(task : Task) {
        // Instanciation d'un objet task avec des données préremplies:
        taskList.add(task)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TaskListFragment.ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
            addTask(task)
            this.myAdapter?.notifyItemChanged(taskList.size)
        }
    }
}