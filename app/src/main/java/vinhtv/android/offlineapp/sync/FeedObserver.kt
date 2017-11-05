package vinhtv.android.offlineapp.sync

import android.arch.lifecycle.MutableLiveData
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import vinhtv.android.offlineapp.datasource.db.FeedContract

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

    }

    fun unsubcribe(contentResolver: ContentResolver) {
        contentResolver.unregisterContentObserver(this)
    }


}