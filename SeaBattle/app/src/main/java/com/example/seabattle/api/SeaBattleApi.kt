package com.example.seabattle.api

import com.example.seabattle.api.model.BooleanResponse
import com.example.seabattle.api.model.StatisticDto
import com.example.seabattle.api.model.UserDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SeaBattleApi {

    @POST("/users/register")
    fun register(@Body userDto: UserDto): Call<BooleanResponse>

    @POST("/users/login")
    fun login(@Body userDto: UserDto): Call<BooleanResponse>

    @GET("/statistic/get/{userName}")
    fun getStatistic(@Path("username") username: String): Call<StatisticDto>

    @POST("/statistic/addStatistic")
    fun addStatistic(@Body statisticDto: StatisticDto): Call<BooleanResponse>
}