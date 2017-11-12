package vinhtv.android.offlineapp.sync

import vinhtv.android.offlineapp.model.FeedItem

/**
 * Created by Admin on 11/5/2017.
 */
class FeedEvent(val event: EVENT, val data: List<FeedItem>) {
    enum class EVENT{ REFRESHED, UPDATED, DELETED }
}