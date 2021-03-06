package com.example.projetandroid.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.projetandroid.R
import com.example.projetandroid.SHARED_PREF_TOKEN_KEY
import com.example.projetandroid.authentication.AuthenticationActivity
import com.example.projetandroid.network.Api
import com.example.projetandroid.tasklist.task.TaskActivity
import com.example.projetandroid.userinfo.UserInfoActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
        const val SET_AVATAR_REQUEST_CODE = 42
        const val TASK = "task"
    }

    var myAdapter : TaskListAdapter? = null

    private val viewModel: TaskListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, TaskListViewModel.Factory(activity.application))
                .get(TaskListViewModel::class.java)
    }

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

        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            myAdapter?.taskList?.clear()
            myAdapter?.taskList?.addAll(it)
            myAdapter?.notifyDataSetChanged()
        })

        // Pour une [RecyclerView] ayant l'id "recycler_view":
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        myAdapter = TaskListAdapter(taskList)
        recyclerView.adapter = myAdapter
        var addButton = view.findViewById<FloatingActionButton>(R.id.addButton)
        var disconnectButton = view.findViewById<FloatingActionButton>(R.id.disconnect)
        var avatarImage = view.findViewById<ImageView>(R.id.avatar_image)

        addButton.setOnClickListener {
            //addTask()
            //myAdapter.notifyItemChanged(taskList.size)
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        disconnectButton.setOnClickListener{
            PreferenceManager.getDefaultSharedPreferences(context).edit {
                putString(SHARED_PREF_TOKEN_KEY, "")
            }
            val intent = Intent(activity, AuthenticationActivity::class.java)
            startActivity(intent)
        }

        avatarImage.setOnClickListener {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)
        }

        myAdapter!!.onDeleteClickListener = { task ->
            val position = taskList.indexOf(task)
            taskList.remove(task)
            lifecycleScope.launch {
                viewModel.deleteTask(task)
            }
            myAdapter!!.notifyItemRemoved(position)
        }

        myAdapter!!.onModifyClickListener = { task ->
            val position = taskList.indexOf(task)
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra("task", task)
            intent.putExtra("position", position)
            startActivityForResult(intent,ADD_TASK_REQUEST_CODE)
            //myAdapter!!.notifyItemChanged(position)
        }
    }

    override fun onResume() {
        super.onResume()
        // Ici on ne va pas gérer les cas d'erreur donc on force le crash avec "!!"
        lifecycleScope.launch {
            val userInfo = Api.INSTANCE.userService.getInfo().body()
            //val userInfo = UserInfo("", "", "")
            var userName = view?.findViewById<TextView>(R.id.user_name)
            userName?.text = "${userInfo?.firstName} ${userInfo?.lastName}"
            var avatarImage = view?.findViewById<ImageView>(R.id.avatar_image)
            avatarImage?.load(userInfo?.avatar) {
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }

        }

        lifecycleScope.launch {
            viewModel.refresh()
        }
    }

    private fun addTask(task : Task) {
        // Instanciation d'un objet task avec des données préremplies:
        taskList.add(task)
        lifecycleScope.launch {
            viewModel.addTask(task)
            viewModel.refresh()
        }
    }

    private fun modifyTask(task : Task , pos : Int) {
        // Instanciation d'un objet task avec des données préremplies:
        taskList[pos] = task
        lifecycleScope.launch {
            viewModel.updateTask(task)
            viewModel.refresh()
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
        /*if (requestCode == SET_AVATAR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch {
                val userInfo = Api.userService.getInfo().body()
                //val userInfo = UserInfo("", "", "")
                var avatarImage = view?.findViewById<ImageView>(R.id.avatar_image)
                if (userInfo != null && userInfo.avatar != null) {
                    avatarImage?.load(userInfo.avatar) {
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }*/
    }
}

