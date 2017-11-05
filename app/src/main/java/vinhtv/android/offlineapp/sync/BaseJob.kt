package vinhtv.android.offlineapp.sync

import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params

/**
 * Created by Admin on 11/4/2017.
 */
abstract class BaseJob(param: Params): Job(param) {
    enum class PRIORITY { HIGH, BACKGROUND }

    protected fun shouldRetry(throwable: Throwable): Boolean {
        if(throwable is NetworkException) return throwable.shouldRetry()
        return false
    }
}