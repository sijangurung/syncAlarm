package com.gurungsijan.syncalarm.devices

import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.common.BaseFragment

/**
 * Created by Sijan Gurung on 24/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
class deviceFragment : BaseFragment() {

    //layout
    override val mContentLayoutResourceId: Int = R.layout.fragment_devices
    //widgets
    //adapters
    //data

    var firebaseDbRef: DatabaseReference? = null

    companion object {
        fun newInstance() = deviceFragment()
    }

    override fun setUp() {
        super.setUp()
        firebaseDbRef = FirebaseDatabase.getInstance().getReference("devices")
    }

    override fun setUpUI(view: View) {
        super.setUpUI(view)

    }
}