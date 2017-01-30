package com.gurungsijan.syncalarm.common

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gurungsijan.syncalarm.common.extensions.tag
import kotlin.properties.Delegates

/**
 * Created by Sijan Gurung on 25/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
abstract class BaseFragment : Fragment() {

    abstract val mContentLayoutResourceId: Int

    //widgets
    var vRoot: View by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)

        //handle savedInstanceState
        if (savedInstanceState != null) {
            handleSavedInstanceState(savedInstanceState)
        }

        //handle arguments
        val args = getArguments();
        if (args != null) {
            handleArguments(args)
        }

        setUp()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        vRoot = inflater!!.inflate(mContentLayoutResourceId, container, false)
        Log.v(tag(), tag() + "::onCreateView()")
        return vRoot
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI(view as View)
        Log.v(tag(), tag() + "::onViewCreated()")
    }

    override fun onResume() {
        super<Fragment>.onResume()
        //EventBus.getDefault().register(this)
        Log.v(tag(), tag() + "::onResume()")
    }

    override fun onPause() {
        super<Fragment>.onPause()
        //EventBus.getDefault().unregister(this)
        Log.v(tag(), tag() + "::onPause()")
    }

    override fun onDestroy() {
        Log.v(tag(), tag() + "::onDestory()")
        super<Fragment>.onDestroy()
    }

    open fun handleSavedInstanceState(savedInstanceState: Bundle) {
    }

    open fun handleArguments(args: Bundle) {
    }

    open fun setUp() {
    }

    open fun setUpUI(view: View) {
    }

    fun getRootView(): View {
        return vRoot
    }

    /*public fun onEvent(event: BaseEvent) {
    }*//*public fun onEvent(event: BaseEvent) {
    }*/
}
