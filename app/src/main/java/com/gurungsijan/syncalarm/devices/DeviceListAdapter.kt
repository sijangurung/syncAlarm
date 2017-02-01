package com.gurungsijan.syncalarm.devices;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.repository.Device
import kotlinx.android.synthetic.main.item_device_request.view.*

/**
 * Created by Sijan Gurung on 30/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
class DeviceListAdapter(var items: List<Device>, val itemPositiveClick: (Device) -> Unit, val itemNegativeClick: (Device) -> Unit) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device_request, parent, false)
        return ViewHolder(view, itemPositiveClick, itemNegativeClick)
    }

    fun setData(newDataSet: List<Device>) {
        items = newDataSet
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size ?: 0


    class ViewHolder(val view: View, val itemPositiveClick: (Device) -> Unit, val itemNegativeClick: (Device) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindView(item: Device) {
            with(item) {

                itemView.txtRequestDevice.text = profile?.model
                itemView.txtRequestID.text = profile?.colorId

                itemView.btnAccept.setOnClickListener { itemPositiveClick(this) }
                itemView.btnReject.setOnClickListener { itemNegativeClick(this) }
            }

        }
    }

}
