package vinhtv.android.offlineapp.sync

import android.arch.lifecycle.MutableLiveData
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import vinhtv.android.offlineapp.datasource.db.AppDatabase
import vinhtv.android.offlineapp.datasource.db.FeedContentProvider
import vinhtv.android.offlineapp.datasource.db.FeedContract
import vinhtv.android.offlineapp.model.FeedItem
import vinhtv.android.offlineapp.model.db.User
import vinhtv.android.offlineapp.util.DataUtils

/**
 * Created by Admin on 11/5/2017.
 */
class FeedObserver(handler: Handler, contentResolver: ContentResolver): ContentObserver(handler) {

    private val observable = MutableLiveData<FeedEvent>()

    fun observable() = observable

    init {
        contentResolver.registerContentObserver(FeedContract.URI_POST, true, this)
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        val code = FeedContentProvider.MATCHER.match(uri)
        val db = AppDatabase.getInstance(null)
        val userDao = db.userDao()
        val postDao = db.postDao()
        when(code) {
            FeedContentProvider.CODE_POST_DIR -> {
                val usersMap = DataUtils.userListAsMap(userDao.getAll())
                val posts = DataUtils.postsFromCursor(postDao.getAll())

                val feeds = posts.map {
                    val user = usersMap[it.userID, User(1, "vinhtv")]
                    FeedItem(user, it)
                }
                observable.postValue(FeedEvent(FeedEvent.EVENT.REFRESHED, feeds))
            }
            FeedContentProvider.CODE_POST_ITEM -> {
                val data = ArrayList<FeedItem>()
                val user: User
                val postId = uri!!.lastPathSegment
                val post = DataUtils.singlePostFromCursor(postDao.get(postId), true)
                val deleteOps = post.isDump()
                if(deleteOps) { // deleted post
                    post.id = postId
                    user = User(-1, "Invalid")
                } else {
                    user = userDao.get(post.userID)!!
                }
                data.add(FeedItem(user, post))
                observable.postValue(FeedEvent(if(deleteOps) FeedEvent.EVENT.DELETED else FeedEvent.EVENT.UPDATED, data))
            }
        }
    }


}