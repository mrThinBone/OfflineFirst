package vinhtv.android.offlineapp.repository

import vinhtv.android.offlineapp.datasource.LocalFeedDataSource
import vinhtv.android.offlineapp.datasource.RemoteFeedDataSource
import vinhtv.android.offlineapp.model.FeedItem
import vinhtv.android.offlineapp.model.db.Post

/**
 * Created by Admin on 11/4/2017.
 */
class FeedRepository(private val local: LocalFeedDataSource,
                     private val remote: RemoteFeedDataSource) {

    fun addPost(post: Post) {
        local.add(post)
        remote.add(post)
    }

    fun livePost(since: Long) = local.liveLoad(since)

    fun getFeeds(posts: List<Post>): List<FeedItem> = local.load(posts)

    fun fetchPosts(since: Long) {
        val fetchResult = remote.fetch(since)
        local.update(fetchResult.first, fetchResult.second)
    }
}