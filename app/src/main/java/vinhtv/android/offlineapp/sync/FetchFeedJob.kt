package vinhtv.android.offlineapp.sync

import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import vinhtv.android.offlineapp.datasource.api.ApiService
import java.util.concurrent.TimeUnit

/**
 * Created by Admin on 11/5/2017.
 */
class FetchFeedJob(params: Params = Params(10).setSingleId("FetchFeedJob").singleInstanceBy("FetchFeedJob").requireNetwork(),
                   private val apiService: ApiService) : BaseJob(params) {
    override fun onRun() {
    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        if(shouldRetry(throwable)) {
            val constraint = RetryConstraint.createExponentialBackoff(runCount, TimeUnit.MINUTES.toMillis(1))
            constraint.setApplyNewDelayToGroup(true)
            return constraint
        }
        return RetryConstraint.CANCEL
    }

    override fun onAdded() {}

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {}

}