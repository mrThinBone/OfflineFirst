package vinhtv.android.offlineapp.datasource.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import vinhtv.android.offlineapp.datasource.db.FeedContract.AUTHORITY
import vinhtv.android.offlineapp.model.db.Post

/**
 * Created by Admin on 11/5/2017.
 */
class FeedContentProvider: ContentProvider() {

    companion object {
        val CODE_POST_DIR = 1
        val CODE_POST_ITEM = 2
        val MATCHER = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        MATCHER.addURI(AUTHORITY, Post.TABLE_NAME, CODE_POST_DIR)
        MATCHER.addURI(AUTHORITY, "${Post.TABLE_NAME}/*", CODE_POST_ITEM)
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        if(values == null) throw IllegalArgumentException("ContentValues not found")
        val code = MATCHER.match(uri)
        when(code) {
            CODE_POST_DIR -> {
                val post = Post.fromContentValues(values)
                val count = AppDatabase.getInstance(context).postDao().insert(post)
                if(count > 0) context.contentResolver.notifyChange(uri, null)
                return uri!!
            }
            CODE_POST_ITEM -> throw IllegalArgumentException("Invalid URI, insert with ID is not supported")
        }
        throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?,
                       selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val code = MATCHER.match(uri)
        if(code == CODE_POST_DIR || code == CODE_POST_ITEM) {
            val postDao = AppDatabase.getInstance(context).postDao()
            val id = postIdFromUri(uri!!)
            val cursor = if(code == CODE_POST_ITEM) postDao.get(id) else postDao.getAll()
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun onCreate(): Boolean = true

    override fun update(uri: Uri?, values: ContentValues?, selection: String?,
                        selectionArgs: Array<out String>?): Int {
        if(values == null) throw IllegalArgumentException("ContentValues not found")
        val code = MATCHER.match(uri)
        when(code) {
            CODE_POST_DIR -> throw IllegalArgumentException("cannot update without ID")
            CODE_POST_ITEM -> {
                val id = postIdFromUri(uri!!)
                val post = Post.fromContentValues(values)
                if(id != post.id) throw IllegalArgumentException("uri ID is not same as post ID")
                AppDatabase.getInstance(context).postDao().update(post)
                context.contentResolver.notifyChange(uri, null)
                return 1
            }
        }
        throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val code = MATCHER.match(uri)
        when(code) {
            CODE_POST_DIR -> throw IllegalArgumentException("delete whole table is not supported")
            CODE_POST_ITEM -> {
                val id = postIdFromUri(uri!!)
                AppDatabase.getInstance(context).postDao().delete(id)
                context.contentResolver.notifyChange(uri, null)
            }
        }
        throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun bulkInsert(uri: Uri?, values: Array<out ContentValues>?): Int {
        if(values == null) throw IllegalArgumentException("Values not found")
        val code = MATCHER.match(uri)
        when(code) {
            CODE_POST_DIR -> {
                val posts = values.map { Post.fromContentValues(it) }
                val count = AppDatabase.getInstance(context).postDao().insert(posts).count { it>0 }
                if(count > 0) context.contentResolver.notifyChange(uri, null)
                return count
            }
            CODE_POST_ITEM -> throw IllegalArgumentException("Invalid URI, insert with ID is not supported")
        }
        throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun getType(uri: Uri?): String {
        when(MATCHER.match(uri)) {
            CODE_POST_DIR -> "vnd.android.cursor.dir/$AUTHORITY.${Post.TABLE_NAME}"
            CODE_POST_ITEM -> "vnd.android.cursor.item/$AUTHORITY.${Post.TABLE_NAME}"
        }
        throw IllegalArgumentException("Unknown URI: $uri")
    }

    private fun postIdFromUri(uri: Uri): String = uri.pathSegments[1]
}