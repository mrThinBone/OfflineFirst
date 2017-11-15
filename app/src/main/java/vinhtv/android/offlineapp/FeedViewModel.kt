package vinhtv.android.offlineapp

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import kotlinx.coroutines.experimental.launch
import vinhtv.android.offlineapp.datasource.LocalFeedDataSource
import vinhtv.android.offlineapp.datasource.RemoteFeedDataSource
import vinhtv.android.offlineapp.model.FeedItem
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User
import vinhtv.android.offlineapp.repository.FeedRepository

/**
 * Created by Admin on 11/1/2017.
 */
class FeedViewModel(context: Application): AndroidViewModel(context) {

    private val user = User(1, "vinhtv")
    private val feedRepository = FeedRepository(local = LocalFeedDataSource(context),
            remote = RemoteFeedDataSource(App.jobManager()))
    private var livePost: LiveData<List<Post>>? = null
    val feedObservable = MutableLiveData<List<FeedItem>>()
    val uiEventObservable = MutableLiveData<UIFeedEvent>()

    fun addFeed(message: String) {
        launch(ThreadPool.commonIO) {
            val post = Post(Post.compositeUniqueKey(user.id), message, created = System.currentTimeMillis()
                    , pending = true, userID = user.id)
            feedRepository.addPost(post)
        }
    }

    fun fetchFeeds() {
        uiEventObservable.postValue(UIFeedEvent.NONE)
        launch(ThreadPool.commonIO) {
            feedRepository.fetchPosts(0)
            uiEventObservable.postValue(UIFeedEvent.REFRESHED)
        }
    }

    fun observeData() {
        if(livePost == null) {
            livePost = feedRepository.livePost(0)
            livePost?.observeForever(livePostObserver)
        }
    }

    override fun onCleared() {
        livePost?.removeObserver(livePostObserver)
        super.onCleared()
    }

    private val livePostObserver = Observer<List<Post>> {
        if(it!= null && it.isNotEmpty()) {
            launch (ThreadPool.diskIO) {
                feedObservable.postValue(feedRepository.getFeeds(it))
            }
        }
    }
}