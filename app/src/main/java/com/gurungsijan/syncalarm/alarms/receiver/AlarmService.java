package com.gurungsijan.syncalarm.alarms.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gurungsijan.syncalarm.database.DbHelper;
import com.gurungsijan.syncalarm.repository.Alarm;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Sijan Gurung on 06/02/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
public class AlarmService extends Service{


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /*
	 * (non-Javadoc)
	 *
	 * @see android.app.Service#onCreate()
	 */
    @Override
    public void onCreate() {
        Log.d(this.getClass().getSimpleName(),"onCreate()");
        super.onCreate();
    }

    private Alarm getNext(){
        Set<Alarm> alarmQueue = new TreeSet<Alarm>(new Comparator<Alarm>() {
            @Override
            public int compare(Alarm lhs, Alarm rhs) {
                int result = 0;
                long diff = lhs.getAlarmTime().getTimeInMillis() - rhs.getAlarmTime().getTimeInMillis();
                if(diff>0){
                    return 1;
                }else if (diff < 0){
                    return -1;
                }
                return result;
            }
        });

        List<Alarm> alarms = DbHelper.getInstance().getAllAlarms();

        for(Alarm alarm : alarms){
            if(alarm.getAlarmActive())
                alarmQueue.add(alarm);
        }
        if(alarmQueue.iterator().hasNext()){
            return alarmQueue.iterator().next();
        }else{
            return null;
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        //DbHelper.getInstance().deactivate();
        super.onDestroy();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(this.getClass().getSimpleName(),"onStartCommand()");
        Alarm alarm = getNext();
        if(null != alarm){
            alarm.schedule(getApplicationContext());
            Log.d(this.getClass().getSimpleName(),alarm.getTimeUntilNextAlarmMessage());

        }else{
            Intent myIntent = new Intent(getApplicationContext(), AlarmBroadcastReciever.class);
            myIntent.putExtra("alarm", new Alarm());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);
        }
        return START_NOT_STICKY;
    }

}
