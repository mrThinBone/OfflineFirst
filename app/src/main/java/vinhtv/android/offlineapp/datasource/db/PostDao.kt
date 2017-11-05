package vinhtv.android.offlineapp.datasource.db

import android.arch.persistence.room.*
import android.database.Cursor
import vinhtv.android.offlineapp.model.db.Post

/**
 * Created by Admin on 11/3/2017.
 */

@Dao interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsert(posts: List<Post>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(post: Post): Int

    @Query("select * from post")
    fun getAll(): Cursor

    @Query("select * from post where id = :id")
    fun get(id: String): Cursor

    @Query("delete from post where id = :id")
    fun delete(id: String)
}