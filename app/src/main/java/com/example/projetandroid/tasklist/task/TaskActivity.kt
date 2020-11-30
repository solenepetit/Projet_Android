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
        val editTaskTitle = findViewById<EditText>(R.id.edit_task_title)
        val editTaskDescription = findViewById<EditText>(R.id.edit_task_desc)

        button.setOnClickListener {
            val newTask = Task(id = UUID.randomUUID().toString(), title = editTaskTitle.text.toString(), description = editTaskDescription.text.toString())
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Companion.TASK_KEY, newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val TASK_KEY = "myTask"
    }
}