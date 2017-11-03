package vinhtv.android.offlineapp.datasource.api

/**
 * Created by Admin on 11/3/2017.
 */
data class IPost(var id: Long, var text: String, var user_id: Long, var client_id: String,
                 var created_at: String, var updated_at: String)