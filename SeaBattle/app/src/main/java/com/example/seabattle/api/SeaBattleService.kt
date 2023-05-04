package com.example.seabattle.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SeaBattleService {

    private var seaBattleApi = createRetrofit().create(SeaBattleApi::class.java);

    public fun getApi(): SeaBattleApi {
        return seaBattleApi;
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("ddddd")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}