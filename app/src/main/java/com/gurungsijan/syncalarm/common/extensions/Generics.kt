package com.gurungsijan.syncalarm.common.extensions

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.util.Log
import com.gurungsijan.syncalarm.R

/**
 * Created by Sijan Gurung on 25/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */

fun <T : Any> T.tag(): String { return javaClass.simpleName }

fun <T> with(t: T, body: T.() -> Unit) { t.body() }

fun Context.CustomDialog(strTitle: String, strMessage: String,
                         strPositiveButton: String, strNegativeButton: String, listener: DialogBoxCallback): Dialog {

    val builder = AlertDialog.Builder(this, R.style.Base_ThemeOverlay_AppCompat_Dialog)
    val messageToshow = strMessage
    val titleToshow = strTitle
    val positiveButtonText = strPositiveButton
    val negativeButtonText = strNegativeButton


    builder.setMessage(messageToshow)
            .setNegativeButton(negativeButtonText, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Log.d(tag(),"Negative Response !")
                    listener.onNegativeResponse()
                }
            }).setPositiveButton(positiveButtonText, object : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            Log.d(tag(),"Positive Response !")
            listener.onPositiveResponse()

        }
    })

    builder.setCancelable(false)
    builder.setTitle(titleToshow)

    return builder.create()
}

interface DialogBoxCallback {
    fun onPositiveResponse()
    fun onNegativeResponse()
}