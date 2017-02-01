package com.gurungsijan.syncalarm.devices

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.alarms.adapter.AlarmListAdapter
import com.gurungsijan.syncalarm.alarms.repository.AlarmItem
import com.gurungsijan.syncalarm.common.BaseFragment
import com.gurungsijan.syncalarm.common.extensions.bindView
import com.gurungsijan.syncalarm.common.extensions.randomBGColor
import com.gurungsijan.syncalarm.common.extensions.toast

/**
 * Created by Sijan Gurung on 24/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 *
 */
class alarmFragment : BaseFragment  () {

    //layout
    override val mContentLayoutResourceId: Int = R.layout.fragment_alarms
    //widgets
    val rvAllAlarms by lazy { vRoot.bindView<RecyclerView>(R.id.rvAllAlarms) }
    //adapters
    var allAlarmsAdapter : AlarmListAdapter? = null
    //data
    val alarmsList = listOf<AlarmItem>(AlarmItem("9:35", randomBGColor())
            ,AlarmItem("7:30", randomBGColor())
            ,AlarmItem("11:30", randomBGColor())
            ,AlarmItem("3:10", randomBGColor())
            ,AlarmItem("10:45", randomBGColor()) )

    var firebaseDbRef : DatabaseReference? = null

    companion object {
        fun newInstance() =  alarmFragment()
    }

    override fun setUp() {
        super.setUp()
        firebaseDbRef = FirebaseDatabase.getInstance().getReference("alarms")

    }

    override fun setUpUI(view: View) {
        super.setUpUI(view)

      //  firebaseDbRef?.setValue(alarmsList)

        val layoutManager = GridLayoutManager(view.context,2)
        rvAllAlarms.setHasFixedSize(true)
        rvAllAlarms.layoutManager = layoutManager

        allAlarmsAdapter = AlarmListAdapter(alarmsList,{
            activity.toast("Cliked the alarm $it")
        })

        rvAllAlarms.adapter = allAlarmsAdapter
    }
}