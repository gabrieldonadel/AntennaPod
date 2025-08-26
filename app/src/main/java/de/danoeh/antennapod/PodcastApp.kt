package de.danoeh.antennapod

import android.app.Application
import android.os.StrictMode
import android.util.Log
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeApplicationEntryPoint
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultReactNativeHost
import com.google.android.material.color.DynamicColors
import expo.modules.ReactNativeHostWrapper
import expo.modules.ReactNativeHostWrapper.Companion.createReactHost
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.EventBusException

/** Main application class.  */
class PodcastApp : Application(), ReactApplication {
    override val reactNativeHost: ReactNativeHost = ReactNativeHostWrapper(
        this,
        object : DefaultReactNativeHost(this) {
            override fun getPackages(): List<ReactPackage> =
                PackageList(this).packages.apply {
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    // add(MyReactNativePackage())
                }

            override fun getJSMainModuleName(): String = ".expo/.virtual-metro-entry"

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

            override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        }
    )

    override val reactHost: ReactHost
        get() = ReactNativeHostWrapper.createReactHost(applicationContext, reactNativeHost)

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler(CrashReportWriter())
        RxJavaErrorHandlerSetup.setupRxJavaErrorHandler()

        if (BuildConfig.DEBUG) {
            val builder = StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDropBox()
                .detectActivityLeaks()
                .detectLeakedClosableObjects()
                .detectLeakedRegistrationObjects()
            StrictMode.setVmPolicy(builder.build())
        }

        try {
            EventBus.builder()
                // .addIndex(ApEventBusIndex())
                .logNoSubscriberMessages(false)
                .sendNoSubscriberEvent(false)
                .installDefaultEventBus()
        } catch (e: EventBusException) {
            Log.d(TAG, e.message!!)
        }

        DynamicColors.applyToActivitiesIfAvailable(this)
        ClientConfigurator.initialize(this)
        PreferenceUpgrader.checkUpgrades(this)

        // Ensure reactHost is created now (safe, Application is attached)
        ReactNativeApplicationEntryPoint.loadReactNative(this)
    }

    companion object {
        private const val TAG = "PodcastApp"
    }
}
