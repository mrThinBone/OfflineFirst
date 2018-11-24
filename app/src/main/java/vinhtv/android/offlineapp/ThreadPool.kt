package vinhtv.android.offlineapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

/**
 * Created by vinh.trinh on 11/1/2017.
 */
object ThreadPool {
    val diskIO = Executors.newSingleThreadExecutor().asCoroutineDispatcher()//Dispatchers.IO
    val commonIO = Dispatchers.Default
    val main = Dispatchers.Main
}