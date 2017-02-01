package com.gurungsijan.syncalarm.app

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.google.firebase.database.FirebaseDatabase
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.preferences.PreferencesMgr
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Sijan Gurung on 25/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */

open class App : Application() {

    operator fun get(c: Context): App {
        return c.applicationContext as App
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        initFonts()
        initialisePreference()

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build())
    }

    fun initFonts() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/roboto/Roboto_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    fun initialisePreference() {
        PreferencesMgr.instance.initPreference(this, "alarmPrefs")
    }

}

