package com.gurungsijan.syncalarm.common

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.common.extensions.CustomDialog
import com.gurungsijan.syncalarm.common.extensions.DialogBoxCallback
import java.util.*

/**
 * Created by Sijan Gurung on 25/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
class RunTimePermissionManager {

    private val REQUEST_CODE = 333
    private val TAG = RunTimePermissionManager::class.java.simpleName
    private var mCheckResult: MutableMap<String, Boolean> = hashMapOf()
    private var mPermissions = HashMap<String, PermissionOptionalDetails?>()
    private var mListeners: MutableSet<(checkResult: MutableMap<String, Boolean>) -> Unit> = hashSetOf()
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null
    private val mContext: Context

    companion object {
        fun checkPermissions(context: Context, mPermissions: Array<String>): Map<String, Boolean> {
            var mCheckResult: HashMap<String, Boolean> = hashMapOf()
            for (permission in mPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager
                        .PERMISSION_GRANTED) {
                    mCheckResult.put(permission, false)
                } else {
                    mCheckResult.put(permission, true)
                }
            }
            return mCheckResult
        }
    }

    constructor(mActivity: Activity) {
        this.mActivity = mActivity
        mContext = mActivity
    }

    constructor(fragment: Fragment) {
        this.mFragment = fragment
        mContext = fragment.context
    }

    fun addPermissionsListener(listener: ((checkResult: MutableMap<String, Boolean>) -> Unit)) {
        mListeners.add(listener)
    }

    fun addPermissions(permissionsWithDetails: Map<String, PermissionOptionalDetails?>) {
        permissionsWithDetails.forEach { mPermissions.put(it.key, it.value) }
    }

    fun makePermissionRequest(shouldClearResults: Boolean = true) {
        if (shouldClearResults) mCheckResult.clear()
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mPermissions.size > 0) {
            executePermissionRequest()
        } else {
            mPermissions.forEach { mCheckResult.put(it.key, true) }
            notifyListeners()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            for (i in permissions.indices) {
                mCheckResult.put(permissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED)
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    val details = mPermissions[permissions[i]]
                    if (details != null) {
                        if (shouldShowRequestPermissionExplanation(permissions[i])) {
                            showExplanation(permissions[i], details)
                            return
                        } else {
                            if (!details.optional) {
                                showExplanationPermissionRequired(details)
                                return
                            }
                        }
                    }
                }
            }
            notifyListeners()
        }
    }

    fun onActivityResult(requestCode: Int) {
        if (requestCode == REQUEST_CODE) {
            makePermissionRequest(false)
        }
    }

    private fun executePermissionRequest() {
        val askPermissions = HashSet<String>()
        for (permission in mPermissions) {
            if (ContextCompat.checkSelfPermission(mContext, permission.key) != PackageManager
                    .PERMISSION_GRANTED) {
                askPermissions.add(permission.key)
            } else {
                mCheckResult.put(permission.key, true)
            }
        }
        if (askPermissions.size > 0) {
            val permissionsArray = arrayOfNulls<String>(askPermissions.size)
            askPermissions.toArray<String>(permissionsArray)
            if (mActivity != null) {
                ActivityCompat.requestPermissions(mActivity!!, permissionsArray, REQUEST_CODE)
            } else if (mFragment?.isAdded ?: false) {
                mFragment!!.requestPermissions(permissionsArray, REQUEST_CODE)
            }
        } else {
            notifyListeners()
        }
    }

    private fun showExplanation(permission: String, details: PermissionOptionalDetails) {

        val dialog: Dialog? = mContext.CustomDialog(details.title,
                details.message,
                mContext.getString(R.string.str_ok),
                mContext.getString(R.string.str_cancel),
                object : DialogBoxCallback {
                    override fun onPositiveResponse() {
                        makePermissionRequest(false)
                    }

                    override fun onNegativeResponse() {
                        if (mPermissions[permission] !is PermissionRequiredDetails) {
                            mPermissions.remove(permission)
                        }
                        //  makePermissionRequest(false)
                    }

                })
        dialog?.setCancelable(true)
        dialog?.show()
    }

    private fun showExplanationPermissionRequired(details: PermissionOptionalDetails) {

        val dialog: Dialog? = mContext.CustomDialog(details.title,
                if (details is PermissionRequiredDetails) details.requiredMessage else details.message,
                mContext.getString(R.string.str_ok),
                mContext.getString(R.string.str_cancel),
                object : DialogBoxCallback {
                    override fun onPositiveResponse() {
                        startSettings()
                    }

                    override fun onNegativeResponse() {
                    }

                })
        dialog?.setCancelable(true)
        dialog?.show()

    }

    private fun startSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val packageName: String = (mFragment?.activity ?: mActivity!!).packageName
        intent.data = Uri.fromParts("package", packageName, null)
        if (mActivity != null) {
            mActivity!!.startActivityForResult(intent, REQUEST_CODE)
        } else {
            mFragment!!.startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun shouldShowRequestPermissionExplanation(permission: String): Boolean {
        if (mActivity != null)
            return ActivityCompat.shouldShowRequestPermissionRationale(mActivity!!, permission)
        else if (mFragment?.isAdded ?: false)
            return mFragment!!.shouldShowRequestPermissionRationale(permission)
        else return false
    }

    private fun notifyListeners() {
        mListeners.forEach { it(mCheckResult) }
        log()
    }

    private fun log() {
        val stringBuilder = StringBuilder()
        mCheckResult.forEach { stringBuilder.append("permission ${it.key} ${it.value}\n") }
        Log.d(TAG, stringBuilder.toString())
    }
}

open class PermissionOptionalDetails(val title: String, val message: String, val optional: Boolean = true)

class PermissionRequiredDetails(title: String, message: String, val requiredMessage: String) :
        PermissionOptionalDetails(title, message, false)