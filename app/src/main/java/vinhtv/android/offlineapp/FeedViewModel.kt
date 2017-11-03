package vinhtv.android.offlineapp

import android.arch.lifecycle.ViewModel
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User

/**
 * Created by Admin on 11/1/2017.
 */
class FeedViewModel: ViewModel() {

    val user = User(1, "vinhtv")

    fun createPost(message: String): Post {
        return Post(Post.compositeUniqueKey(user.id), message, created = System.currentTimeMillis()
                , pending = true, userID = user.id)
    }

}