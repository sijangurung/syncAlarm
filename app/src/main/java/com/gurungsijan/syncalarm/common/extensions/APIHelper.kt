package com.gurungsijan.syncalarm.common.extensions

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat

/**
 * Created by Sijan Gurung on 30/11/2016.
 * Shortcut AS
 * sijan.gurung@no.no
 */

object ApiHelper {

    val AT_LEAST_16 = Build.VERSION.SDK_INT >= 16
    val AT_LEAST_23 = Build.VERSION.SDK_INT >= 23
    val AT_LEAST_24 = Build.VERSION.SDK_INT >= 24
    val HAS_GETCOLOR_METHOD = Build.VERSION.SDK_INT >= 23


    /**
     * Method to check if the color value should be from the old or the new API....
     * @param context context of the application / activity
     * @param resID  ID of the resource to get the color value....
     */
    @Suppress("DEPRECATION")
    fun getColorById(context: Context, resID: Int): Int {
        if (HAS_GETCOLOR_METHOD)
            return ContextCompat.getColor(context, resID)
        else
            return context.resources.getColor(resID)
    }

}