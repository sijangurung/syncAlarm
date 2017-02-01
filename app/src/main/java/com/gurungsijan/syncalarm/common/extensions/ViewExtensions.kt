package com.gurungsijan.syncalarm.common.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import java.util.*

/**
 * Created by Sijan Gurung on 25/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */

val View.ctx: Context
    get() = context

fun <T : View> View.bindView(id: Int): T {
    val view = findViewById(id) ?:
            throw IllegalArgumentException("Given ID $id could not be found in $this!")
    @Suppress("unchecked_cast")
    return view as T
}

fun Int.dpToPx(context: Context) = this * context.resources.displayMetrics.density

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun randomBGColor(): Int {
    val colorList = listOf<String>("#1976D2", "#2196F3", "#BBDEFB", "#03A9F4", "#212121", "#BDBDBD")
    val randomNumber = Random().nextInt(colorList.size) + 0
    return Color.parseColor(colorList.get(randomNumber))
}