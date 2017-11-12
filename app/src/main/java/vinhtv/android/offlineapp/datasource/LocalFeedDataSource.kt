package vinhtv.android.offlineapp.datasource

import android.arch.lifecycle.LiveData
import android.content.Context
import vinhtv.android.offlineapp.datasource.db.AppDatabase
import vinhtv.android.offlineapp.model.FeedItem
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User
import vinhtv.android.offlineapp.util.DataUtils

/**
 * Created by Admin on 11/3/2017.
 */
class LocalFeedDataSource(context: Context) {

    private val db: AppDatabase = AppDatabase.getInstance(context)

    fun add(post: Post) {
        // user always already exist
        val postDao = db.postDao()
        postDao.insert(post)
    }

    /** GET RID OF CONTENT_PROVIDER THANKS TO LIVE_DATA AND ROOM */
    fun load(): List<FeedItem> {
        val userDao = db.userDao()
        val postDao = db.postDao()
        val usersMap = DataUtils.userListAsMap(userDao.getAll())
        val posts = DataUtils.postsFromCursor(postDao.getAll())

        return posts.map {
            val user = usersMap[it.userID, User(1, "me")]
            FeedItem(user, it)
        }
    }

    fun liveLoad(since: Long): LiveData<List<Post>> = db.postDao().getByTime(since)

    fun update(users: List<User>, posts: List<Post>) {
        val userDao = db.userDao()
        val postDao = db.postDao()
        userDao.bulkInsert(users)
        postDao.bulkInsert(posts)
    }

    fun load(posts: List<Post>): List<FeedItem> {
        val userDao = db.userDao()
        val usersMap = DataUtils.userListAsMap(userDao.getAll())

        return posts.map {
            val user = usersMap[it.userID, User(1, "me")]
            FeedItem(user, it)
        }
    }
}