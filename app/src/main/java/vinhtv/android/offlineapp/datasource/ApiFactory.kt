package vinhtv.android.offlineapp.datasource

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import vinhtv.android.offlineapp.datasource.api.ApiService

/**
 * Created by vinh.trinh on 11/15/2017.
 */
class ApiFactory {
    companion object {
        val apiService = Retrofit.Builder()
                .baseUrl("https://architecture-demo-vinhtv.c9users.io")
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(ApiService::class.java)!!
    }
}