package vinhtv.android.offlineapp

import android.arch.lifecycle.ViewModel
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User
import java.util.*

/**
 * Created by Admin on 11/1/2017.
 */
class FeedViewModel: ViewModel() {

    val user = User(1, "vinhtv")
    private var generateID: Long = 0

    fun createPost(message: String): Post {
        generateID++
        return Post(generateID, message, created = System.currentTimeMillis(),
                clientID = UUID.randomUUID().toString(), pending = true, userID = user.id)
    }

}