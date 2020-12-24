package com.example.projetandroid.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Query("select * from databasetask")
    fun getTasks() : LiveData<List<DatabaseTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tasks: List<DatabaseTask>)

    @Query("DELETE FROM databasetask")
    fun deleteAll()
}

@Dao
interface UserInfoDao {
    @Query("select * from databaseuserinfo")
    fun getUsers() : LiveData<List<DatabaseUserInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users : List<DatabaseUserInfo>)

    @Query("delete from databaseuserinfo")
    fun deleteAll()
}

@Database(entities = [DatabaseTask::class], version = 1)
abstract class TasksDatabase: RoomDatabase() {
    abstract val taskDao: TaskDao
}

@Database(entities = [DatabaseUserInfo::class], version = 1)
abstract class UsersDatabase: RoomDatabase() {
    abstract val userInfoDao: UserInfoDao
}

private lateinit var INSTANCE : TasksDatabase
private lateinit var USERINSTANCE : UsersDatabase

fun getDatabase(context: Context) : TasksDatabase {
    synchronized(TasksDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TasksDatabase::class.java,
                "tasks"
            ).build()
        }
    }
    return INSTANCE
}

fun getUserDatabase(context : Context) : UsersDatabase {
    synchronized(UsersDatabase::class.java) {
        if (!::USERINSTANCE.isInitialized) {
            USERINSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    UsersDatabase::class.java,
                    "users"
            ).build()
        }
    }
    return USERINSTANCE
}