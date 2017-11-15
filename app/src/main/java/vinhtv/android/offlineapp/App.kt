package vinhtv.android.offlineapp

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.config.Configuration
import vinhtv.android.offlineapp.datasource.db.AppDatabase
import vinhtv.android.offlineapp.model.db.User

/**
 * Created by Admin on 11/4/2017.
 */
class App: Application() {

    companion object {
        private var jobManager: JobManager? = null
        fun jobManager() = jobManager!!
    }

    override fun onCreate() {
        super.onCreate()
        val config = Configuration.Builder(this)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .build()
        jobManager = JobManager(config)
        CreateUserTask(this).execute()
    }

    private class CreateUserTask(val context: Context): AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg p0: Void?): Int {
            val me = User(1, "vinhtv")
            AppDatabase.getInstance(context).userDao().insert(me)
            return 0
        }

    }
}