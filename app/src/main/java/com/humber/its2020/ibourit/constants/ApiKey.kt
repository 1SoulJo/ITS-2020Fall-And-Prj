package com.humber.its2020.ibourit.constants

import com.humber.its2020.ibourit.BuildConfig

interface ApiKey {
    companion object {
        const val API_KEY = BuildConfig.MY_API_KEY
    }
}