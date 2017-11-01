package vinhtv.android.offlineapp

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.newSingleThreadContext

/**
 * Created by vinh.trinh on 11/1/2017.
 */
object ThreadPool {
    val diskIO = newSingleThreadContext("diskPool")
    val commonIO = CommonPool
    val main = UI
}