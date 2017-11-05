package vinhtv.android.offlineapp

import android.app.Application
import android.arch.persistence.room.Room
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.config.Configuration
import vinhtv.android.offlineapp.datasource.db.AppDatabase

/**
 * Created by Admin on 11/4/2017.
 */
class App: Application() {

    companion object {
        private var database: AppDatabase? = null
        private var jobManager: JobManager? = null

        fun database() = database
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "feed.db").build()
        val config = Configuration.Builder(this)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .build()
        jobManager = JobManager(config)
    }
}