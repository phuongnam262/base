package com.lock.smartlocker

import android.app.Application
import android.content.Context
import android.os.Process
import com.lock.smartlocker.data.db.AppDatabase
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.NetworkConnectionInterceptor
import com.lock.smartlocker.data.network.services.FaceServives
import com.lock.smartlocker.data.network.services.HardwareControlServices
import com.lock.smartlocker.data.network.services.LockerServives
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.data.repositories.StartAppRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.admin_dashboard.AdminDashboardViewModelFactory
import com.lock.smartlocker.ui.admin_login.AdminLoginViewModelFactory
import com.lock.smartlocker.ui.cart.CartViewModelFactory
import com.lock.smartlocker.ui.category.CategoryViewModelFactory
import com.lock.smartlocker.ui.category_consumable.CategoryConsumableViewModelFactory
import com.lock.smartlocker.ui.collect_items.CollectItemViewModelFactory
import com.lock.smartlocker.ui.deposit_item.DepositItemViewModelFactory
import com.lock.smartlocker.ui.face_list.FaceListViewModelFactory
import com.lock.smartlocker.ui.home.HomeViewModelFactory
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberViewModelFactory
import com.lock.smartlocker.ui.inputemail.InputEmailViewModelFactory
import com.lock.smartlocker.ui.inputotp.InputOTPViewModelFactory
import com.lock.smartlocker.ui.manage_locker.ManageLockerViewModelFactory
import com.lock.smartlocker.ui.manager_menu.ManagerMenuViewModelFactory
import com.lock.smartlocker.ui.menu_register.MenuRegisterViewModelFactory
import com.lock.smartlocker.ui.recognize_face.RecognizeFaceViewModelFactory
import com.lock.smartlocker.ui.register_face.RegisterFaceViewModelFactory
import com.lock.smartlocker.ui.retrieve.RetrieveViewModelFactory
import com.lock.smartlocker.ui.returns.ReturnViewModelFactory
import com.lock.smartlocker.ui.scan_item.ScanItemViewModelFactory
import com.lock.smartlocker.ui.scan_work_card.ScanWorkCardViewModelFactory
import com.lock.smartlocker.ui.consumable_available_locker.ConsumableAvailableLockerViewModelFactory
import com.lock.smartlocker.ui.select_available_locker.SelectAvailableLockerViewModelFactory
import com.lock.smartlocker.ui.select_faulty.SelectFaultyViewModelFactory
import com.lock.smartlocker.ui.setting.SettingViewModelFactory
import com.lock.smartlocker.ui.splash.SplashViewModelFactory
import com.lock.smartlocker.ui.thank.ThankViewModelFactory
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
        bind() from provider { HardwareControlServices(instance()) }

        //bind Repository
        bind() from singleton { UserFaceRepository(instance(), instance()) }
        bind() from singleton { StartAppRepository(instance()) }
        bind() from singleton { ManagerRepository(instance()) }
        bind() from singleton { ReturnRepository(instance()) }
        bind() from singleton { HardwareControllerRepository(instance()) }
        bind() from singleton { LoanRepository(instance()) }

        //bind Factory
        bind() from provider { HomeViewModelFactory(instance(), instance()) }
        bind() from provider { SplashViewModelFactory(instance()) }
        bind() from provider { ManagerMenuViewModelFactory(instance()) }
        bind() from provider { InputEmailViewModelFactory(instance()) }
        bind() from provider { AdminLoginViewModelFactory(instance()) }
        bind() from provider { InputOTPViewModelFactory(instance()) }
        bind() from provider { RegisterFaceViewModelFactory(instance()) }
        bind() from provider { AdminDashboardViewModelFactory(instance()) }
        bind() from provider { ReturnViewModelFactory(instance()) }
        bind() from provider { InputSerialNumberViewModelFactory(instance()) }
        bind() from provider { SelectFaultyViewModelFactory() }
        bind() from provider { SelectAvailableLockerViewModelFactory(instance()) }
        bind() from provider { DepositItemViewModelFactory(instance(), instance()) }
        bind() from provider { ThankViewModelFactory() }
        bind() from provider { CategoryViewModelFactory(instance()) }
        bind() from provider { CartViewModelFactory(instance()) }
        bind() from provider { RecognizeFaceViewModelFactory(instance(), instance()) }
        bind() from provider { CollectItemViewModelFactory(instance(), instance()) }
        bind() from provider { ScanItemViewModelFactory(instance()) }
        bind() from provider { FaceListViewModelFactory(instance()) }
        bind() from provider { SettingViewModelFactory(instance()) }
        bind() from provider { ManageLockerViewModelFactory(instance(), instance()) }
        bind() from provider { RetrieveViewModelFactory(instance(), instance()) }
        bind() from provider { MenuRegisterViewModelFactory() }
        bind() from provider { ScanWorkCardViewModelFactory(instance()) }
        bind() from provider { CategoryConsumableViewModelFactory(instance()) }
        bind() from provider { ConsumableAvailableLockerViewModelFactory(instance()) }
    }

}