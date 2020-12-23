package com.example.projetandroid.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.projetandroid.tasklist.Task
import java.security.AccessControlContext

@Dao
interface TaskDao {
    @Query("select * from DatabaseTasks")
    fun getTasks() : LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tasks : List<DatabaseTasks>)
}

@Database(entities = [DatabaseTasks::class], version = 1)
abstract class TasksDatabase: RoomDatabase() {
    abstract val taskDao: TaskDao
}

private lateinit var INSTANCE : TasksDatabase

fun getDatabase(context: Context) : TasksDatabase {
    synchronized(TasksDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, TasksDatabase::class.java,
                "tasks").build()
        }
    }
    return INSTANCE
}