package com.humber.its2020.ibourit.constants

import android.content.Context
import com.humber.its2020.ibourit.constants.AppConstant.SP_API
import com.humber.its2020.ibourit.constants.AppConstant.SP_KEY_API

object Urls {
    private const val BASE_URL = "http://10.0.2.2:8080"
    const val IMAGE_BASE = "$BASE_URL/%s"

    fun getBaseUrl(ctx: Context): String? {
        val sp = ctx.getSharedPreferences(SP_API, 0)
        return sp.getString(SP_KEY_API, BASE_URL)
    }
}