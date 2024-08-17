package com.lock.smartlocker.data.network.services

import retrofit2.Retrofit

interface HardwareControlServives {
    companion object {
        fun create(retrofit: Retrofit): HardwareControlServives {
            return retrofit.create(HardwareControlServives::class.java)
        }
    }

}