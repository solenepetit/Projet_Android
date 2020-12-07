package com.example.projetandroid.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projetandroid.R
import com.example.projetandroid.network.Api
import com.example.projetandroid.network.TasksRepository
import com.example.projetandroid.tasklist.task.TaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
        const val TASK = "task"
    }

    var myAdapter : TaskListAdapter? = null

    private val tasksRepository = TasksRepository()

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

        tasksRepository.taskList.observe(viewLifecycleOwner, Observer {
            myAdapter?.taskList?.clear()
            myAdapter?.taskList?.addAll(it)
            myAdapter?.notifyDataSetChanged()
        })

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
            lifecycleScope.launch {
                tasksRepository.deleteTask(task)
            }
            myAdapter!!.notifyItemRemoved(position)
        }

        myAdapter!!.onModifyClickListener = { task ->
            val position = taskList.indexOf(task)
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra("task", task)
            intent.putExtra("position", position)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
            //myAdapter!!.notifyItemChanged(position)
        }
    }

    override fun onResume() {
        super.onResume()
        // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()
            //val userInfo = UserInfo("", "", "")
            var userName = view?.findViewById<TextView>(R.id.user_name)
            userName?.text = "${userInfo?.firstName} ${userInfo?.lastName}"
        }

        lifecycleScope.launch {
            tasksRepository.refresh()
        }
    }

    private fun addTask(task : Task) {
        // Instanciation d'un objet task avec des données préremplies:
        taskList.add(task)
        lifecycleScope.launch {
            tasksRepository.addTask(task)
        }
    }

    private fun modifyTask(task : Task , pos : Int) {
        // Instanciation d'un objet task avec des données préremplies:
        taskList[pos] = task
        lifecycleScope.launch {
            tasksRepository.updateTask(task)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TaskListFragment.ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
            val modified = data!!.getIntExtra(TaskActivity.MODIFIED,0)
            if( modified == 0){
                addTask(task)
                this.myAdapter?.notifyItemChanged(taskList.size)
            }
            else{
                val position = data!!.getIntExtra("position",taskList.size)
                modifyTask(task, position)
                this.myAdapter?.notifyItemChanged(position)
            }
        }
    }
}

