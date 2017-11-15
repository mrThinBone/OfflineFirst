package vinhtv.android.offlineapp.util

import android.database.Cursor
import android.util.LongSparseArray
import vinhtv.android.offlineapp.model.db.Post
import vinhtv.android.offlineapp.model.db.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Admin on 11/4/2017.
 */
class DataUtils {

    companion object {
        fun userListAsMap(users: List<User>): LongSparseArray<User> {
            val map = LongSparseArray<User>()
            users.forEach { map.put(it.id, it) }
            return map
        }

        fun timeInMillis(dateString: String): Long {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)
            return dateFormat.parse(dateString).time
        }

        fun postsFromCursor(cursor: Cursor, autoClose: Boolean = true): List<Post> {
            val posts = ArrayList<Post>()
            while (cursor.moveToNext()) {
                posts.add(singlePostFromCursor(cursor))
            }
            if(autoClose) cursor.close()
            return posts
        }

        fun singlePostFromCursor(cursor: Cursor, autoClose: Boolean = false): Post {
            if(autoClose) {
                if(!cursor.moveToFirst()) return Post.dumpPost()
            }
            val post = Post(
                    id = cursor.getString(cursor.getColumnIndex(Post.COL_ID)),
                    text = cursor.getString(cursor.getColumnIndex(Post.COL_TEXT)),
                    created = cursor.getLong(cursor.getColumnIndex(Post.COL_CREATED_AT)),
                    pending = cursor.getInt(cursor.getColumnIndex(Post.COL_PENDING)) == 1,
                    userID = cursor.getLong(cursor.getColumnIndex(Post.COL_USER_ID)))
            if(autoClose) cursor.close()
            return post
        }
    }
}