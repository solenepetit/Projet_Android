package com.example.projetandroid.tasklist.task

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projetandroid.MainActivity
import com.example.projetandroid.R
import com.example.projetandroid.tasklist.Task
import com.example.projetandroid.tasklist.TaskListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*



class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        var button = findViewById<Button>(R.id.validate_button)
        var editTaskTitle = findViewById<EditText>(R.id.edit_task_title)
        var editTaskDescription = findViewById<EditText>(R.id.edit_task_desc)

        var id_t =  UUID.randomUUID().toString()


        val task = intent!!.getSerializableExtra(TaskListFragment.TASK) as? Task
        var modified = 0
        if(task != null){
            editTaskTitle.setText(task.title)
            editTaskDescription.setText(task.description)
            id_t = task.id
            modified = 1
        }

        button.setOnClickListener {

            val newTask = Task(id = id_t, title = editTaskTitle.text.toString(), description = editTaskDescription.text.toString())
            intent.putExtra(Companion.TASK_KEY, newTask)
            intent.putExtra( MODIFIED, modified)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val TASK_KEY = "myTask"
        const val MODIFIED = "modified"
    }
}