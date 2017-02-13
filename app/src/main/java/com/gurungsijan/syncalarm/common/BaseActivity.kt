package com.gurungsijan.syncalarm.common

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.gurungsijan.syncalarm.alarms.receiver.AlarmBroadcastReciever


/**
 * Created by Sijan Gurung on 25/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
open class BaseActivity : AppCompatActivity() {

    private var mProgressDialog: ProgressDialog? = null

    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.setMessage("Loading...")
        }

        mProgressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.dismiss()
        }
    }

    val uid: String
        get() = FirebaseAuth.getInstance().getCurrentUser()!!.getUid()



    fun alarmScheduleService(){
        val mathAlarmServiceIntent = Intent(this, AlarmBroadcastReciever::class.java)
        sendBroadcast(mathAlarmServiceIntent, null)
    }

}