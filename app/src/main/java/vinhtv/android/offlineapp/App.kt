package vinhtv.android.offlineapp

import android.app.Application
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.config.Configuration

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
    }
}