package com.gurungsijan.syncalarm.alarms.adapter;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gurungsijan.syncalarm.R
import com.gurungsijan.syncalarm.alarms.repository.AlarmItem
import kotlinx.android.synthetic.main.alarm_item.view.*

/**
 * Created by Sijan Gurung on 25/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
class AlarmListAdapter(var items: List<AlarmItem>, val itemClick: (AlarmItem) -> Unit) : RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    fun setData(newDataSet: MutableList<AlarmItem>) {
        items = newDataSet.toList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size ?: 0


    class ViewHolder(val view: View, val itemClick: (AlarmItem) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindView(item: AlarmItem) {
            with(item) {
                itemView.txtAlarmTime.text = time
                itemView.img_bg.setBackgroundColor(bgColor)

                itemView.setOnClickListener { itemClick(this) }
            }

        }
    }

}
