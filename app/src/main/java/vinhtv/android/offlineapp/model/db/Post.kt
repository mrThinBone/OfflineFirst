package vinhtv.android.offlineapp.model.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by Admin on 11/1/2017.
 */
@Entity(tableName = "post", foreignKeys = arrayOf(
        ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userID"))
))
class Post(@PrimaryKey @ColumnInfo(name = "id") var id: String,
           @ColumnInfo(name = "text") var text: String,
           @ColumnInfo(name = "createdAt") var created: Long,
           @ColumnInfo(name = "pending") var pending: Boolean,
           @ColumnInfo(name = "userID") var userID: Long) {

    fun same(other: Post?): Boolean {
        if(other == null) return false
        if(id != other.id) return false
        if(text != other.text) return false
        if(userID != other.userID) return false
        return pending == other.pending
    }

    companion object {
        fun compositeUniqueKey(userID: Long) = "$userID/${UUID.randomUUID()}"
    }
}