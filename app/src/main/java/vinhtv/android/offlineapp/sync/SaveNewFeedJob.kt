package vinhtv.android.offlineapp.sync

import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import vinhtv.android.offlineapp.datasource.api.ApiService
import vinhtv.android.offlineapp.datasource.db.AppDatabase
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.util.DataUtils.Companion.timeInMillis
import java.util.concurrent.TimeUnit

/**
 * Created by Admin on 11/4/2017.
 */
class SaveNewFeedJob(params: Params = Params(10).requireNetwork().groupBy("new_post").persist(),
                     private val post: Post,
                     private val localDB: AppDatabase,
                     private val apiService: ApiService): BaseJob(params) {

    override fun onRun() {
        val call = apiService.sendPost(post.text, post.id, post.userID)
        val response = call.execute()
        if(response.isSuccessful) {
            val responseBody = response.body()
            if(responseBody != null) {
                post.created = timeInMillis(responseBody.post.created_at)
                localDB.postDao().update(post)
            }
        } else {
            throw NetworkException(response.code())
        }
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

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {
        localDB.postDao().delete(post)
    }

}