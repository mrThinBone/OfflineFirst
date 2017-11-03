package vinhtv.android.offlineapp.model.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Admin on 11/1/2017.
 */
@Entity(tableName = "user")
class User(@PrimaryKey @ColumnInfo(name = "id") var id: Long,
           @ColumnInfo(name = "name") var name: String)