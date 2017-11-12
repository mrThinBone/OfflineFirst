package vinhtv.android.offlineapp.datasource.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User

/**
 * Created by Admin on 11/3/2017.
 */
@Database(entities = arrayOf(User::class, Post::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        private var mInstance: AppDatabase? = null

        fun getInstance(context: Context?): AppDatabase {
            if (mInstance == null)
                mInstance = Room.databaseBuilder(context!!, AppDatabase::class.java, "feed.db").build()
            return mInstance!!
        }
    }
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
}