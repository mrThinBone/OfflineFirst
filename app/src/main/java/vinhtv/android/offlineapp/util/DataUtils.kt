package vinhtv.android.offlineapp.util

import vinhtv.android.offlineapp.model.db.User
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Admin on 11/4/2017.
 */
class DataUtils {

    companion object {
        fun userListAsMap(users: List<User>): Map<Long, User> {
            val map = HashMap<Long, User>()
            users.forEach { map.put(it.id, it) }
            return map
        }

        fun timeInMillis(dateString: String): Long {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
            return dateFormat.parse(dateString).time
        }
    }
}