package vinhtv.android.offlineapp.datasource.db

import android.arch.persistence.room.*
import vinhtv.android.offlineapp.model.db.Post

/**
 * Created by Admin on 11/3/2017.
 */

@Dao interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(post: Post)

    @Query("select * from post")
    fun getAll(): List<Post>

    @Query("select * from post where id = :id")
    fun get(id: String): Post?
}