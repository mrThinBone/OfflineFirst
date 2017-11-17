package vinhtv.android.offlineapp

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.os.HandlerThread
import kotlinx.coroutines.experimental.launch
import vinhtv.android.offlineapp.datasource.LocalFeedDataSource
import vinhtv.android.offlineapp.datasource.RemoteFeedDataSource
import vinhtv.android.offlineapp.model.FeedItem
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User
import vinhtv.android.offlineapp.repository.FeedRepository
import vinhtv.android.offlineapp.sync.FeedEvent
import vinhtv.android.offlineapp.sync.FeedObserver

/**
 * Created by Admin on 11/1/2017.
 */
class FeedViewModel(context: Application): AndroidViewModel(context) {

    private val user = User(1, "vinhtv")
    private val feedRepository = FeedRepository(local = LocalFeedDataSource(context),
            remote = RemoteFeedDataSource(App.jobManager()))
    val uiEventObservable = MutableLiveData<UIFeedEvent>()

    // observer Post table from content provider
    private var feedObserver: FeedObserver? = null

    fun addFeed(message: String): FeedItem {
        val post = Post(Post.compositeUniqueKey(user.id), message, created = System.currentTimeMillis()
                , pending = true, userID = user.id)
        launch(ThreadPool.commonIO) {
            feedRepository.addPost(post)
        }
        return FeedItem(user, post)
    }

    fun getLocalFeed() {
        launch(ThreadPool.commonIO) {
            feedObserver?.observable()?.postValue(FeedEvent(FeedEvent.EVENT.REFRESHED, feedRepository.getFeeds()))
        }
    }

    fun fetchFeeds() {
        uiEventObservable.postValue(UIFeedEvent.NONE)
        launch(ThreadPool.commonIO) {
            feedRepository.fetchPosts(0)
            getLocalFeed()
            uiEventObservable.postValue(UIFeedEvent.REFRESHED)
        }
    }

    fun observeData(): MutableLiveData<FeedEvent>? {
        if(feedObserver == null) {
            val threadHandler = HandlerThread("DbHandlerThread")
            threadHandler.start()
            feedObserver = FeedObserver(Handler(threadHandler.looper), contentResolver())
        }
        return feedObserver?.observable()
    }

    override fun onCleared() {
        contentResolver().unregisterContentObserver(feedObserver)
        super.onCleared()
    }

    private fun contentResolver() = getApplication<Application>().contentResolver
}