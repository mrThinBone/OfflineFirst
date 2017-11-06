package vinhtv.android.offlineapp.datasource

import vinhtv.android.offlineapp.datasource.db.AppDatabase
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User
import vinhtv.android.offlineapp.util.DataUtils

/**
 * Created by Admin on 11/3/2017.
 */
class LocalFeedDataSource {

    private val db: AppDatabase = AppDatabase.getInstance(null)!!

    fun add(post: Post) {
        // user always already exist
        val postDao = db.postDao()
        postDao.insert(post)
    }

    fun load(): Pair<List<User>, List<Post>> {
        val userDao = db.userDao()
        val postDao = db.postDao()
        return Pair(userDao.getAll(), DataUtils.postsFromCursor(postDao.getAll()))
    }
}