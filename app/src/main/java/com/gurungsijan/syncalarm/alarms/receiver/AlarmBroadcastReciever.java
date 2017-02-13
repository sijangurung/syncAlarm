package com.gurungsijan.syncalarm.alarms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Sijan Gurung on 06/02/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
public class AlarmBroadcastReciever  extends BroadcastReceiver{

    private static final String TAG = "AlarmBroadcastReciever";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);
    }
}
