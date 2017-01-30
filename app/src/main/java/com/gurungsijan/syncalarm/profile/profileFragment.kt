package com.gurungsijan.syncalarm.profile

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.common.BaseFragment
import com.gurungsijan.syncalarm.common.extensions.ApiHelper
import com.gurungsijan.syncalarm.common.extensions.bindView
import com.gurungsijan.syncalarm.common.extensions.hide
import com.gurungsijan.syncalarm.common.extensions.show
import com.gurungsijan.syncalarm.preferences.PreferencesMgr
import com.gurungsijan.syncalarm.repository.Profile
import com.gurungsijan.syncalarm.repository.Register
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*



/**
 * Created by Sijan Gurung on 25/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
class profileFragment : BaseFragment() {

    //layout
    override val mContentLayoutResourceId: Int = R.layout.fragment_profile
    //widgets
    val btnRegisterMe by lazy { vRoot.bindView<Button>(R.id.btnRegisterDevice) }
    val imgProfile by lazy { vRoot.bindView<ImageView>(R.id.imgProfile) }
    val txtProfileId by lazy { vRoot.bindView<TextView>(R.id.txtProfileID) }

    //data
    var firebaseDbRef: DatabaseReference? = null
    var preference = PreferencesMgr.instance

    var profileObject: Profile? = null

    companion object {
        fun newInstance() = profileFragment()
    }

    override fun setUp() {
        super.setUp()
    }

    override fun setUpUI(view: View) {
        super.setUpUI(view)

        //Checking if the user has previous data...
        if (preference.getRegisteredStatus() == "no") {
            btnRegisterDevice.show()
        } else {
            btnRegisterDevice.hide()
            profileObject = Gson().fromJson(preference.profileData, Profile::class.java)
            setProfileUI(profileObject)
        }


        //on button press..
        btnRegisterMe.setOnClickListener {
            //registration part!!!!
            firebaseDbRef = FirebaseDatabase.getInstance().getReference("register")
            firebaseDbRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    println("No Data $p0")
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    println("Data: $p0")
                    var strHexColor: String? = "000000"
                    if (p0 != null) {
                        if (p0.value == null) {
                            //writing the first device!!
                            println("Value is null")
                            strHexColor = "000000"
                            firebaseDbRef?.child(strHexColor)?.setValue("registered")


                        } else {
                            val currentValue = p0.value as HashMap<String, String>
                            println("Current Value = ${currentValue.keys}")
                            val lastKey = currentValue.keys.max()
                            println("LastKey: $lastKey")
                            val converted = Integer.parseInt(lastKey, 16) + 1
                            strHexColor = String.format("%06X", 0xFFFFFF and converted)
                        }

                        writeDeviceDetails(strHexColor)
                        preference.changeRegisteredStatus(true)
                    }
                }

            })
        }
    }

    fun setProfileUI(profileObject: Profile?) {

        val colorActive = Color.BLACK

        txtProfileDevice.setTextColor(colorActive)
        lblProfileColor.setTextColor(colorActive)
        lblProfileID.setTextColor(colorActive)
        txtProfileID.setTextColor(ApiHelper.getColorById(context, R.color.colorAccent))

        txtProfileDevice.text = profileObject?.model
        val colorId: String = profileObject?.colorId ?: "000000"
        txtProfileId.text = colorId
        changeProfileColorUI(colorId)

        val animation = AlphaAnimation(1.0f, 0.0f)
        animation.fillAfter = true
        animation.duration = 200
        btnRegisterDevice.startAnimation(animation)


    }

    fun changeProfileColorUI(strHexColor: String) {
        imgProfile.setBackgroundColor(Color.parseColor("#$strHexColor"))
    }

    fun writeDeviceDetails(strHexColor: String) {
        firebaseDbRef = FirebaseDatabase.getInstance().getReference("register")
        val newRegister = Register(strHexColor)
        firebaseDbRef?.child(strHexColor)?.setValue(newRegister)

        firebaseDbRef = FirebaseDatabase.getInstance().getReference("alarmData")

        val newProfile = Profile(Build.SERIAL, Build.PRODUCT, Build.MANUFACTURER, Build.MODEL, strHexColor)

        firebaseDbRef?.child(strHexColor)?.child("profile")?.setValue(newProfile)
        preference.profileData = Gson().toJson(newProfile)
        setProfileUI(newProfile)

    }
}