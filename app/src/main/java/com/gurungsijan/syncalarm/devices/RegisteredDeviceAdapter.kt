package com.gurungsijan.syncalarm.devices;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.common.extensions.hide
import com.gurungsijan.syncalarm.common.extensions.show
import com.gurungsijan.syncalarm.repository.Device
import com.gurungsijan.syncalarm.repository.Profile
import kotlinx.android.synthetic.main.item_registered_device.view.*

/**
 * Created by Sijan Gurung on 30/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
class RegisteredDeviceAdapter(var items: List<Device>, val itemClick: (Device) -> Unit) : RecyclerView.Adapter<RegisteredDeviceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_registered_device, parent, false)
        return ViewHolder(view, itemClick)
    }

    fun setData(newDataSet: MutableList<Device>) {

        val addDevice: Device = Device()
        //always there...
        addDevice.profile = Profile("","","","register new","new")
        addDevice.isRegistered = false

        items = newDataSet.plus(addDevice)

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size ?: 0


    class ViewHolder(val view: View, val itemClick: (Device) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindView(item: Device) {
            with(item) {
                if (profile?.colorId == "new") {
                    itemView.txtRegisteredID.hide()
                    itemView.imgRegisteredDevice.setImageResource(R.drawable.ic_device_add)
                } else{
                    itemView.txtRegisteredID.show()
                    itemView.imgRegisteredDevice.setImageResource(R.drawable.img_mobile)
                }

                itemView.txtRegisteredDevice.text = profile?.model
                itemView.txtRegisteredID.text = profile?.colorId

                itemView.setOnClickListener { itemClick(this) }
            }

        }
    }

}
