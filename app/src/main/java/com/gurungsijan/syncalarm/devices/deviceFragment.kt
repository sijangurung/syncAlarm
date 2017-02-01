package com.gurungsijan.syncalarm.devices

import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.common.BaseFragment
import com.gurungsijan.syncalarm.common.extensions.bindView
import com.gurungsijan.syncalarm.common.extensions.hide
import com.gurungsijan.syncalarm.common.extensions.show
import com.gurungsijan.syncalarm.common.extensions.toast
import com.gurungsijan.syncalarm.preferences.PreferencesMgr
import com.gurungsijan.syncalarm.repository.Device
import com.gurungsijan.syncalarm.repository.Profile


/**
 * Created by Sijan Gurung on 24/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
class deviceFragment : BaseFragment() {

    //layout
    override val mContentLayoutResourceId: Int = R.layout.fragment_devices
    //widgets
    val rvDeviceRequests by lazy { getRootView().bindView<RecyclerView>(R.id.rvDeviceRequests) }
    val rvRegisteredDevices by lazy { getRootView().bindView<RecyclerView>(R.id.rvRegisteredDevices) }

    var lblStatus : TextView? = null
    var myTxtSearchID : TextView? = null
    var myTxtSearchDevice : TextView? = null
    var myBtnConfirm : Button? = null


    var alertDialog : AlertDialog? = null
    //adapters
    var deviceRequestsAdapter: DeviceListAdapter? = null
    var registeredDeviceAdapter: RegisteredDeviceAdapter? = null
    //data
    var preference = PreferencesMgr.instance

    var deviceId: String? = null
    var deviceIdToRequest: String? = null


    var deviceRequestsList = mutableListOf<Device>()
    var registeredDevicesList = mutableListOf<Device>()

    var firebaseDbRef: DatabaseReference? = null

    companion object {
        fun newInstance() = deviceFragment()
    }

    override fun setUp() {
        super.setUp()
        deviceId = preference.deviceID

    }

    override fun setUpUI(view: View) {
        super.setUpUI(view)

        rvDeviceRequests.layoutManager = LinearLayoutManager(view.context)
        rvDeviceRequests.setNestedScrollingEnabled(false)

        rvRegisteredDevices.layoutManager = GridLayoutManager(view.context, 2)
        rvRegisteredDevices.setNestedScrollingEnabled(false)

        deviceRequestsAdapter = DeviceListAdapter(emptyList(), {
            //positive
            changeRegisteredState(it, true)

        }, {
            //negative
            changeRegisteredState(it, false)

        })
        rvDeviceRequests.adapter = deviceRequestsAdapter
        deviceRequestsAdapter?.setData(deviceRequestsList)

        registeredDeviceAdapter = RegisteredDeviceAdapter(emptyList(), {
            if (it.profile?.colorId == "new") {
                //adding new...
                showRegisterDialog()
                //registerNewDevice()
            }
        })
        rvRegisteredDevices.adapter = registeredDeviceAdapter
        registeredDeviceAdapter?.setData(registeredDevicesList)

        //Setting listener for devices node..
        setDeviceListenerNode()

    }

    fun setDeviceListenerNode() {

        firebaseDbRef = FirebaseDatabase.getInstance().getReference("alarmData")

        firebaseDbRef?.child(deviceId)?.child("devices")?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {
                    val deviceRequests = arrayListOf<Device>()
                    val registeredDevices = arrayListOf<Device>()

                    for (deviceDataSnap in dataSnapshot.getChildren()) {
                        val device: Device? = deviceDataSnap.getValue(Device::class.java)
                        if (device != null) {
                            if (device.isRegistered == false)
                                deviceRequests.add(device)
                            else
                                registeredDevices.add(device)
                        }
                    }

                    deviceRequestsAdapter?.setData(deviceRequests)
                    registeredDeviceAdapter?.setData(registeredDevices)

                }
            }

        })
    }

    fun changeRegisteredState(device: Device, isAllowedORNot: Boolean = false) {

        firebaseDbRef = FirebaseDatabase.getInstance().getReference("alarmData").child(deviceId)
        val devicesRef = firebaseDbRef?.child("devices")?.child(device.profile?.colorId)
        device.isRegistered = true

        if (isAllowedORNot)
            devicesRef?.setValue(device)
        else
            devicesRef?.removeValue()

    }

    fun SearchDatabaseForID(ID: String) {
        firebaseDbRef = FirebaseDatabase.getInstance().getReference("alarmData")
        firebaseDbRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot != null) {
                    if (snapshot.hasChild(ID)){
                        lblStatus?.text = "Device Found"
                        val profile = snapshot.child(ID).child("profile").getValue(Profile::class.java)
                        ShowDeviceDetails(profile)
                        myBtnConfirm?.isEnabled = true
                        myTxtSearchID?.show()
                        myTxtSearchDevice?.show()
                    }
                    else{
                        lblStatus?.text = "No Device Found"
                        myTxtSearchID?.text = ""
                        myTxtSearchDevice?.text = ""
                        myTxtSearchID?.hide()
                        myTxtSearchDevice?.hide()
                        myBtnConfirm?.isEnabled = false
                    }

                }
            }
        })
    }

    fun ShowDeviceDetails(profileObject: Profile){
        myTxtSearchID?.text = profileObject.colorId
        myTxtSearchDevice?.text = profileObject.model

        myBtnConfirm?.setOnClickListener {
            registerNewDevice(profileObject)
            alertDialog?.dismiss()
        }
    }

    fun registerNewDevice(newProfileObject: Profile) {

        deviceIdToRequest = newProfileObject.colorId


        firebaseDbRef = FirebaseDatabase.getInstance().getReference("alarmData").child(deviceIdToRequest).child("devices").child(deviceId)

        val thisProfileObject = Gson().fromJson(preference.profileData, Profile::class.java)
        val newDevice = Device(thisProfileObject, false)

        //Check if the device is pre-registered
        firebaseDbRef?.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onDataChange(snapshot: DataSnapshot?) {
                if(snapshot != null){
                    val device : Device? = snapshot.getValue(Device::class.java)
                    if(device?.isRegistered == true){
                        activity.toast("Already Registered")
                    }else{
                        activity.toast("Request Sent!")
                        firebaseDbRef?.setValue(newDevice)
                    }
                }
            }

        })


    }

    fun showRegisterDialog() {
        val view = activity.layoutInflater.inflate(R.layout.dialog_register_device, null)
        alertDialog = AlertDialog.Builder(context).create()

        alertDialog?.setTitle("Register Device")
        alertDialog?.setIcon(R.drawable.img_mobile)
        alertDialog?.setCancelable(true)

        val edtDeviceID = view.findViewById(R.id.edt_deviceID) as EditText
        lblStatus = view.findViewById(R.id.lblStatusDevice) as TextView
        myTxtSearchID = view.findViewById(R.id.txtSearchID) as TextView
        myTxtSearchDevice = view.findViewById(R.id.txtSearchDevice) as TextView
        myBtnConfirm = view.findViewById(R.id.btnConfirm) as Button

        edtDeviceID.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text.toString().length == 6) {
                    if( text.toString() == deviceId)
                        lblStatus?.text = "Cannot Register your own device"
                    else
                        SearchDatabaseForID(text.toString())
                }else{
                    lblStatus?.text = "No Device Found"
                    myBtnConfirm?.isEnabled = false
                    myTxtSearchID?.hide()
                    myTxtSearchDevice?.hide()
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })

        alertDialog?.setView(view);
        alertDialog?.show();
    }
}