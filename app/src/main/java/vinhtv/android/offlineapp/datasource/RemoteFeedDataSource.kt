package vinhtv.android.offlineapp.datasource

import com.birbit.android.jobqueue.JobManager
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User
import vinhtv.android.offlineapp.sync.SaveNewFeedJob
import vinhtv.android.offlineapp.util.DataUtils.Companion.timeInMillis
import java.io.IOException
import java.io.InvalidObjectException

/**
 * Created by Admin on 11/3/2017.
 */
class RemoteFeedDataSource(private val jobManager: JobManager) {

    fun add(post: Post) {
        jobManager.addJob(SaveNewFeedJob(post = post))
    }

    fun fetch(since: Long): Pair<List<User>, List<Post>> {
        val call = ApiFactory.apiService.feed(since)
        try {
            val response = call.execute()
            val responseBody = response.body() ?: throw InvalidObjectException("fetch feed response body not found")
            val userList = responseBody.users.map {
                User(it.id, it.name)
            }
            val postList = responseBody.posts.map {
                Post(it.client_id, it.text, timeInMillis(it.created_at), false, it.user_id)
            }
            return Pair(userList, postList)
        } catch (e: IOException) {
            throw e
        }
    }
}