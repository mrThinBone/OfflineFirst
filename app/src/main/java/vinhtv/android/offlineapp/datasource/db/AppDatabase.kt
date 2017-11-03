package vinhtv.android.offlineapp.datasource.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User

/**
 * Created by Admin on 11/3/2017.
 */
@Database(entities = arrayOf(User::class, Post::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
}