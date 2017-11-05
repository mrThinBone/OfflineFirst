package vinhtv.android.offlineapp.datasource

import com.birbit.android.jobqueue.JobManager
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import vinhtv.android.offlineapp.App
import vinhtv.android.offlineapp.datasource.api.ApiService
import vinhtv.android.offlineapp.datasource.api.IPost
import vinhtv.android.offlineapp.datasource.api.IUser
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

    companion object {
        val apiService = Retrofit.Builder()
                .baseUrl("https://architecture-demo-vinhtv.c9users.io")
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(ApiService::class.java)!!
    }

    fun add(post: Post) {
        jobManager.addJob(SaveNewFeedJob(post = post, localDB = App.database()!!,
                apiService = apiService))
    }

    fun fetch(since: Long): Pair<List<User>, List<Post>> {
        val call = apiService.feed(since)
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