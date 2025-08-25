package de.danoeh.antennapod;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import static com.facebook.react.ReactNativeApplicationEntryPoint.loadReactNative;
import com.facebook.react.defaults.DefaultReactNativeHost;
import com.facebook.react.ReactHost;

import androidx.annotation.NonNull;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.google.android.material.color.DynamicColors;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import java.util.List;

import expo.modules.ReactNativeHostWrapper;

/** Main application class. */
public class PodcastApp extends Application implements ReactApplication {
    private static final String TAG = "PodcastApp";

    private final ReactNativeHost reactNativeHost = new ReactNativeHostWrapper(
            this,
            new DefaultReactNativeHost(this) {
                @Override
                public List<ReactPackage> getPackages() {
                    return new PackageList(this).getPackages();
                }

                @Override
                public String getJSMainModuleName() {
                    return ".expo/.virtual-metro-entry";
                }

                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                public boolean isNewArchEnabled() {
                    return BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
                }

                @Override
                public boolean isHermesEnabled() {
                    return BuildConfig.IS_HERMES_ENABLED;
                }
            });

    // DON'T initialize reactHost here (Application not yet attached). Create lazily.
    private ReactHost reactHost;

    @Override
    public ReactNativeHost getReactNativeHost() {
        return reactNativeHost;
    }

    // Lazily create reactHost when needed. Safe if called before onCreate().
    public synchronized ReactHost getReactHost() {
        if (reactHost == null) {
            // use 'this' (Application) as context; at this point it's safe because caller
            // likely runs after Application is attached, but we also call this in onCreate below.
            reactHost = ReactNativeHostWrapper.createReactHost(this, reactNativeHost);
        }
        return reactHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        loadReactNative(this);
        Thread.setDefaultUncaughtExceptionHandler(new CrashReportWriter());
        RxJavaErrorHandlerSetup.setupRxJavaErrorHandler();

        if (BuildConfig.DEBUG) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .penaltyLog()
                    .penaltyDropBox()
                    .detectActivityLeaks()
                    .detectLeakedClosableObjects()
                    .detectLeakedRegistrationObjects();
            StrictMode.setVmPolicy(builder.build());
        }

        try {
            EventBus.builder()
                    .addIndex(new ApEventBusIndex())
                    .logNoSubscriberMessages(false)
                    .sendNoSubscriberEvent(false)
                    .installDefaultEventBus();
        } catch (EventBusException e) {
            Log.d(TAG, e.getMessage());
        }

        DynamicColors.applyToActivitiesIfAvailable(this);
        ClientConfigurator.initialize(this);
        PreferenceUpgrader.checkUpgrades(this);

        // Ensure reactHost is created now (safe, Application is attached)
        reactHost = ReactNativeHostWrapper.createReactHost(getApplicationContext(), reactNativeHost);
    }
}
