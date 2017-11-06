package vinhtv.android.offlineapp.datasource.db

import android.net.Uri

/**
 * Created by Admin on 11/5/2017.
 */
object FeedContract {
    val AUTHORITY = "vinhtv.android.offlineapp.provider"
    /** The URI for the Post table. */
    val URI_POST = Uri.parse("content://$AUTHORITY/post")!!
}