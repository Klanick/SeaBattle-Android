package com.example.seabattle.api

import com.example.seabattle.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SeaBattleService {

    private val url = "http://" + R.string.localhostAddress;

    private var seaBattleApi = createRetrofit().create(SeaBattleApi::class.java);

    fun getApi(): SeaBattleApi {
        return seaBattleApi;
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
                // Это надо засунуть в про
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}