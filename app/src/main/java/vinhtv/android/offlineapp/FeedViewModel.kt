package vinhtv.android.offlineapp

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User

/**
 * Created by Admin on 11/1/2017.
 */
class FeedViewModel(context: Application): AndroidViewModel(context) {

    val user = User(1, "vinhtv")

    fun createPost(message: String): Post {
        return Post(Post.compositeUniqueKey(user.id), message, created = System.currentTimeMillis()
                , pending = true, userID = user.id)
    }

}