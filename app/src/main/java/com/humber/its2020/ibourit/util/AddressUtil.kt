package com.humber.its2020.ibourit.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.*

object AddressUtil {
    fun coordinateToAddress(ctx: Context, lat: Double, lng: Double): Address {
        val addresses: List<Address?>
        val geocoder = Geocoder(ctx, Locale.getDefault())
        addresses = geocoder.getFromLocation(lat, lng, 1)

        return addresses[0]!!
    }
}