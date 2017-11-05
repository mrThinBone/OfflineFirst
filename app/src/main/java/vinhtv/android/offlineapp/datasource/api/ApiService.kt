package vinhtv.android.offlineapp.datasource.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Admin on 11/3/2017.
 */
interface ApiService {

    @GET("/feed.json")
    fun feed(@Query("since") since: Long): Call<FeedResponse>

    @GET("/user_feed/{userId}.json")
    fun userFeed(@Path("userId") userId: Long, @Query("since") since: Long): Call<FeedResponse>

    @POST("/new_post.json")
    fun sendPost(@Query("text") text: String, @Query("client_id") clientId: String,
                 @Query("user_id") userId: Long): Call<NewPostResponse>
}