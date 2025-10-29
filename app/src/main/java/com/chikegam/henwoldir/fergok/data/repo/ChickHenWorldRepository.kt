package com.chikegam.henwoldir.fergok.data.repo

import android.util.Log
import com.chikegam.henwoldir.fergok.domain.model.ChickHenWorldEntity
import com.chikegam.henwoldir.fergok.domain.model.ChickHenWorldParam
import com.chikegam.henwoldir.fergok.presentation.app.ChickHenWorldApp.Companion.CHICK_HEN_WORLD_MAIN_TAG
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChickHenWorldApi {
    @Headers("Content-Type: application/json")
    @POST("config.php")
    fun getClient(
        @Body jsonString: JsonObject,
    ): Call<ChickHenWorldEntity>
}


private const val CHICK_HEN_WORLD_MAIN = "https://chiickhenworld.com/"
class ChickHenWorldRepository {

    suspend fun chickHenWorldGetClient(
        chickHenWorldParam: ChickHenWorldParam,
        chickHenWorldConversion: MutableMap<String, Any>?
    ): ChickHenWorldEntity? {
        val gson = Gson()
        val api = chickHenWorldGetApi(CHICK_HEN_WORLD_MAIN, null)

        val chickHenWorldJsonObject = gson.toJsonTree(chickHenWorldParam).asJsonObject
        chickHenWorldConversion?.forEach { (key, value) ->
            val element: JsonElement = gson.toJsonTree(value)
            chickHenWorldJsonObject.add(key, element)
        }
        return try {
            val chickHenWorldRequest: Call<ChickHenWorldEntity> = api.getClient(
                jsonString = chickHenWorldJsonObject,
            )
            val chickHenWorldResult = chickHenWorldRequest.awaitResponse()
            Log.d(CHICK_HEN_WORLD_MAIN_TAG, "Retrofit: Result code: ${chickHenWorldResult.code()}")
            if (chickHenWorldResult.code() == 200) {
                Log.d(CHICK_HEN_WORLD_MAIN_TAG, "Retrofit: Get request success")
                Log.d(CHICK_HEN_WORLD_MAIN_TAG, "Retrofit: Code = ${chickHenWorldResult.code()}")
                Log.d(CHICK_HEN_WORLD_MAIN_TAG, "Retrofit: ${chickHenWorldResult.body()}")
                chickHenWorldResult.body()
            } else {
                null
            }
        } catch (e: java.lang.Exception) {
            Log.d(CHICK_HEN_WORLD_MAIN_TAG, "Retrofit: Get request failed")
            Log.d(CHICK_HEN_WORLD_MAIN_TAG, "Retrofit: ${e.message}")
            null
        }
    }


    private fun chickHenWorldGetApi(url: String, client: OkHttpClient?) : ChickHenWorldApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }


}
