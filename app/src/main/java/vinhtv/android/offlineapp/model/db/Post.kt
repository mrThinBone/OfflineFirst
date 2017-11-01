package vinhtv.android.offlineapp.model.db

/**
 * Created by Admin on 11/1/2017.
 */
class Post(var id: Long, var text: String, var created: Long, var pending: Boolean,
           var clientID: String, var userID: Long) {
    fun same(other: Post?): Boolean {
        if(other == null) return false
        if(id != other.id) return false
        if(text != other.text) return false
        if(userID != other.userID) return false
        return pending == other.pending
    }

    fun compositeUniqueKey() = "$userID/$clientID"
}