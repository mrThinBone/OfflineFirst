package vinhtv.android.offlineapp.sync

/**
 * Created by Admin on 11/4/2017.
 */
class NetworkException(val errorCode: Int): RuntimeException() {

    fun shouldRetry() = errorCode < 400 || errorCode > 499

}