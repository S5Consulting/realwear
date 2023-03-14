package com.akso.realwear.app.app

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.akso.realwear.app.repository.RepositoryFactory
import com.akso.realwear.service.OfflineWorkerUtil
import com.sap.cloud.mobile.foundation.crash.CrashService
import com.sap.cloud.mobile.foundation.logging.LoggingService
import com.sap.cloud.mobile.foundation.mobileservices.MobileService
import com.sap.cloud.mobile.foundation.mobileservices.SDKInitializer
import com.sap.cloud.mobile.foundation.settings.SharedDeviceService
import com.sap.cloud.mobile.foundation.settings.policies.LogPolicy
import com.sap.cloud.mobile.foundation.usage.UsageService


class SAPWizardApplication: Application() {

    internal var isApplicationUnlocked = true
    lateinit var preferenceManager: SharedPreferences

    /**
     * Application-wide RepositoryFactory
     */
    lateinit var repositoryFactory: RepositoryFactory
        private set

    override fun onCreate() {
        super.onCreate()
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        repositoryFactory = RepositoryFactory()
        initServices()

    }


    /**
     * Clears all user-specific data and configuration from the application, essentially resetting it to its initial
     * state.
     *
     * If client code wants to handle the reset logic of a service, here is an example:
     *
     *   SDKInitializer.resetServices { service ->
     *       return@resetServices if( service is PushService ) {
     *           PushService.unregisterPushSync(object: CallbackListener {
     *               override fun onSuccess() {
     *               }
     *
     *               override fun onError(p0: Throwable) {
     *               }
     *           })
     *           true
     *       } else {
     *           false
     *       }
     *   }
     */
    fun resetApplication() {
        preferenceManager.also {
            it.edit().clear().apply()
        }
        isApplicationUnlocked = true
        repositoryFactory.reset()
        SDKInitializer.resetServices()
        OfflineWorkerUtil.resetOffline(this)
    }

    private fun initServices() {
        val services = mutableListOf<MobileService>()
        services.add(LoggingService(autoUpload = false).apply {
            policy = LogPolicy(logLevel = "WARN", entryExpiry = 0, maxFileNumber = 4)
            logToConsole = true
        })
        services.add(UsageService())
        services.add(CrashService(false))
        services.add(SharedDeviceService(OFFLINE_APP_ENCRYPTION_CONSTANT))

        SDKInitializer.start(this, * services.toTypedArray(),
                pubKey = "-----BEGIN PUBLIC KEY-----\n" +
"MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEArXY/JDqQvp93X4zDXv8e\n" +
"2T56OWKSuWcxuUoN4qwbt6ijis2tdw/Z/yeKSn3lujvHVNf8ED/MUiHlRH395CoW\n" +
"m9URt1Gv0uxa6gEabHTjOlcvfLxXwCl1a2xMVKrFmCyevWYP8lCNlvnIzJllXm0g\n" +
"TIZtpfIyzdLYrktQ8Q4zVvXLIfQbylOVWLaYG6FqkoJvhRz+0nLIdEB9GbG83Gmc\n" +
"BIyYtrWpbZKbUm7nBjkEoH1Ge+ylZLyHI3MK6zrd4z4BZf8jSPwcBLITIymrxN/s\n" +
"O14Fs4RrGw3atQw92MErykLQDi3OkbPAAqNVtLwAE+gjA2iStJlGY+CdavnGX9cV\n" +
"X2BOa9haDS/jAeboDBQLny4WIc5Gejqn6YWAymgJcx1Bmh5VHoBuBTUiFNP59yIR\n" +
"Kbs/LJaBuIilyiHdpmXrSJ5KHeGbB4kyDemREe0X6QHhdpwdkGfYqm+389nREsBT\n" +
"zPxEtYJ0pRYdTwlVUtOc7uMglWB1iIB7KT1OJwReCvKLAgMBAAE=\n" +
"-----END PUBLIC KEY-----"
        )
    }

    companion object {
        const val KEY_LOG_SETTING_PREFERENCE = "key.log.settings.preference"
        private const val OFFLINE_APP_ENCRYPTION_CONSTANT = "34dab53fc060450280faeed44a36571b"
    }

}