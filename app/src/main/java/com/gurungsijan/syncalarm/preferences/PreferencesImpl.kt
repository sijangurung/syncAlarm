package com.gurungsijan.syncalarm.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Sijan Gurung on 20/06/16.
 * Shortcut AS
 * sijan.gurung@no.no
 */
class PreferencesMgr private constructor() {

    private object Holder {
        val INSTANCE = PreferencesMgr()
    }


    companion object {

        val instance: PreferencesMgr by lazy { Holder.INSTANCE }
        var prefs: SharedPreferences? = null
        const val REGISTERED_STATUS = "registered_status"

        const val PROFILE_DATA = "PROFILE_DATA"
        const val DEVICE_ID = "DEVICE_ID"


        const val BASE_URL = "base_url"
        const val API_KEY = "api_key"
        const val CURRENT_REGION = "current_region"
        const val MEMBERSHIP_NO = "membership_no"
        const val IS_BENEFITS_LOADED = "is_benefit_loaded"

    }

    fun initPreference(context: Context, preferencesName: String) {
        prefs = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    /*registered status*/
    fun changeRegisteredStatus(isRegistered: Boolean) {
        if (isRegistered)
            storeStringToPreferences(REGISTERED_STATUS, "yes")
        else
            storeStringToPreferences(REGISTERED_STATUS, "no")
    }

    fun getRegisteredStatus() = prefs?.getString(REGISTERED_STATUS, "no")

    var deviceID: String
        get() {
            return prefs?.getString(DEVICE_ID, "") ?: ""
        }
        set(value) {
            storeStringToPreferences(DEVICE_ID, value)
        }

    var profileData: String
        get() {
            return prefs?.getString(PROFILE_DATA, "") ?: ""
        }
        set(value) {
            storeStringToPreferences(PROFILE_DATA, value)
        }
    /*GENERAL METHODS...*/
    /**
     * Storing String to Preference
     *
     * @param CONST_NAME name to be stored
     * @param CONST_Val  value to be stored
     */
    fun storeStringToPreferences(CONST_NAME: String, CONST_Val: String) {

        prefs?.edit()
                ?.putString(CONST_NAME, CONST_Val)
                ?.commit()
    }

    /**
     * For Clearing the whole preference....
     */

    fun clearAll() {
        prefs?.edit()?.clear()?.commit()
    }
}
