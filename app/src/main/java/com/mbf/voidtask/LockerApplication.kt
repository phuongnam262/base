package com.lock.basesource

import LocaleHelper
import android.app.Application
import android.content.Context
import android.os.Process
import com.lock.basesource.data.db.AppDatabase
import com.lock.basesource.data.network.AppAPI
import com.lock.basesource.data.network.NetworkConnectionInterceptor
import com.lock.basesource.data.network.services.AppServives
import com.lock.basesource.data.repositories.AppRepository
import com.lock.basesource.ui.home.HomeViewModelFactory
import com.lock.basesource.ui.inputotp.InputOTPViewModelFactory
import com.lock.basesource.ui.splash.SplashViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class LockerApplication : Application(), KodeinAware {

    /**
     * root handling of exception
     */
    private val unCaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, _ ->
        try {
            Process.killProcess(Process.myPid())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(unCaughtExceptionHandler)
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@LockerApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { AppAPI(instance()) }
        bind() from singleton { AppDatabase(instance()) }

        //bind Service
        bind() from provider { AppServives(instance()) }

        //bind Repository
        bind() from singleton { AppRepository(instance()) }

        //bind Factory
        bind() from provider { SplashViewModelFactory() }
        bind() from provider { HomeViewModelFactory() }
        bind() from provider { InputOTPViewModelFactory(instance()) }
    }

}