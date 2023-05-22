package com.example.seabattle

import androidx.lifecycle.ViewModel
import com.example.seabattle.api.model.StatisticDto

class ProfileViewModel : ViewModel() {
    var statistic: StatisticDto? = null
}