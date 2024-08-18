package com.lock.smartlocker

import android.app.Application
import android.os.Process
import com.lock.smartlocker.data.db.AppDatabase
import com.lock.smartlocker.data.network.NetworkConnectionInterceptor
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.services.FaceServives
import com.lock.smartlocker.data.network.services.HardwareControlServives
import com.lock.smartlocker.data.network.services.LockerServives
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.StartAppRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.admin_login.AdminLoginViewModelFactory
import com.lock.smartlocker.ui.facedetect.FaceViewModelFactory
import com.lock.smartlocker.ui.home.HomeViewModelFactory
import com.lock.smartlocker.ui.inputemail.InputEmailViewModel
import com.lock.smartlocker.ui.inputemail.InputEmailViewModelFactory
import com.lock.smartlocker.ui.inputotp.InputOTPViewModelFactory
import com.lock.smartlocker.ui.manager_menu.ManagerMenuViewModelFactory
import com.lock.smartlocker.ui.openlocker.OpenLockerViewModelFactory
import com.lock.smartlocker.ui.register_face.RegisterFaceViewModelFactory
import com.lock.smartlocker.ui.splash.SplashViewModelFactory
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
    private val unCaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, ex ->
        try {
            Process.killProcess(Process.myPid())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate() {
        super.onCreate()
        // set default handler exception to catch
        PreferenceHelper.loadPreferences(applicationContext)
        Thread.setDefaultUncaughtExceptionHandler(unCaughtExceptionHandler)
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@LockerApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { LockerAPI(instance()) }
        bind() from singleton { AppDatabase(instance()) }

        //bind Service
        bind() from provider { LockerServives(instance()) }
        bind() from provider { FaceServives(instance()) }
        bind() from provider { HardwareControlServives.create(instance()) }

        //bind Repository
        bind() from singleton { UserFaceRepository(instance(), instance()) }
        bind() from singleton { StartAppRepository(instance(), instance()) }
        bind() from singleton { ManagerRepository(instance(), instance()) }

        //bind Factory
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from provider { FaceViewModelFactory(instance()) }
        bind() from provider { OpenLockerViewModelFactory(instance()) }
        bind() from provider { SplashViewModelFactory(instance()) }
        bind() from provider { ManagerMenuViewModelFactory(instance()) }
        bind() from provider { InputEmailViewModelFactory(instance()) }
        bind() from provider { AdminLoginViewModelFactory(instance()) }
        bind() from provider { InputOTPViewModelFactory(instance()) }
        bind() from provider { RegisterFaceViewModelFactory(instance()) }
    }

}