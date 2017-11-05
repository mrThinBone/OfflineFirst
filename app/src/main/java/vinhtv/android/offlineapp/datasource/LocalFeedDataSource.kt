package vinhtv.android.offlineapp.datasource

import vinhtv.android.offlineapp.App
import vinhtv.android.offlineapp.datasource.db.AppDatabase
import vinhtv.android.offlineapp.model.FeedItem
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User
import vinhtv.android.offlineapp.util.DataUtils

/**
 * Created by Admin on 11/3/2017.
 */
class LocalFeedDataSource {

    private val db: AppDatabase = App.database()!!

    fun add(post: Post) {
        // user always already exist
        val postDao = db.postDao()
        postDao.insert(post)
    }

    fun load(): List<FeedItem> {
        val userDao = db.userDao()
        val postDao = db.postDao()
        val usersMap = DataUtils.userListAsMap(userDao.getAll())
        val posts = postDao.getAll()

        return posts.map {
            val user = usersMap.getOrDefault(it.userID, User(1, "me"))
            FeedItem(user, it)
        }
    }
}