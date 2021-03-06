package keijumt.androidbase

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.Log
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import keijumt.androidbase.di.Injectable
import keijumt.androidbase.di.Injector
import timber.log.Timber

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return Injector.setup(this)
    }

    override fun onCreate() {
        super.onCreate()

        setupActivityLifecycle()
        setupLog()
    }

    private fun handleActivity(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity) /// Inject!!!!
        }
        (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentCreated(fm: FragmentManager?, f: Fragment?,
                                                   savedInstanceState: Bundle?) {
                        if (f is Injectable) {
                            AndroidSupportInjection.inject(f)
                        }
                    }
                }, true)
    }

    /**
     * ログ出力セットアップ
     */
    private fun setupLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Log.i(TAG, "timber.planet normal.")
        } else {
            Timber.plant(Timber.DebugTree())
        }
    }


    /**
     * Activity ライフサイクル カスタマイズ
     */
    private fun setupActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, bundle: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, bundle: Bundle?) {
                activity?.let {
                    handleActivity(it)
                }
            }

        })
    }
}