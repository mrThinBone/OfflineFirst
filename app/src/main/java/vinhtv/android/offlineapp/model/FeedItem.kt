package vinhtv.android.offlineapp.model

import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User

/**
 * Created by Admin on 11/1/2017.
 */
data class FeedItem(val user: User, val post: Post)