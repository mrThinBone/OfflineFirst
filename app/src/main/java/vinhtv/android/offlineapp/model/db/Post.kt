package vinhtv.android.offlineapp.model.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues
import vinhtv.android.offlineapp.model.db.Post.Companion.TABLE_NAME
import java.util.*

/**
 * Created by Admin on 11/1/2017.
 */
@Entity(tableName = TABLE_NAME, foreignKeys = arrayOf(
        ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userID"))
))
class Post(@PrimaryKey @ColumnInfo(name = COL_ID) var id: String,
           @ColumnInfo(name = COL_TEXT) var text: String,
           @ColumnInfo(name = COL_CREATED_AT) var created: Long,
           @ColumnInfo(name = COL_PENDING) var pending: Boolean,
           @ColumnInfo(name = COL_USER_ID) var userID: Long) {

    fun same(other: Post?): Boolean {
        if(other == null) return false
        if(id != other.id) return false
        if(text != other.text) return false
        if(userID != other.userID) return false
        return pending == other.pending
    }

    fun toContentValues(): ContentValues {
        val values = ContentValues()
        values.put(COL_ID, id)
        values.put(COL_TEXT, text)
        values.put(COL_CREATED_AT, created)
        values.put(COL_PENDING, pending)
        values.put(COL_USER_ID, userID)
        return values
    }

    companion object {
        const val TABLE_NAME = "post"
        const val COL_ID = "id"
        const val COL_TEXT = "text"
        const val COL_CREATED_AT = "createdAt"
        const val COL_PENDING = "pending"
        const val COL_USER_ID = "userID"

        fun compositeUniqueKey(userID: Long) = "$userID/${UUID.randomUUID()}"

        fun fromContentValues(values: ContentValues): Post {
            return Post(values.getAsString(COL_ID),
                    values.getAsString(COL_TEXT),
                    values.getAsLong(COL_CREATED_AT),
                    values.getAsBoolean(COL_PENDING),
                    values.getAsLong(COL_USER_ID))
        }
    }
}