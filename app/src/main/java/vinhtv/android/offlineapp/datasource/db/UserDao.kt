package vinhtv.android.offlineapp.datasource.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import vinhtv.android.offlineapp.model.db.User

/**
 * Created by Admin on 11/3/2017.
 */
@Dao interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("select * from user where id = :id")
    fun get(id: Long): User?

    @Query("select * from user")
    fun getAll(): List<User>
}