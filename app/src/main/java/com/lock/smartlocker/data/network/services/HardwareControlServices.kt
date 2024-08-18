package com.lock.smartlocker.data.network.services

import retrofit2.Retrofit

interface HardwareControlServices {
    companion object {
        operator fun invoke(retrofit: Retrofit): HardwareControlServices {
            return retrofit.create(HardwareControlServices::class.java)
        }
    }

}