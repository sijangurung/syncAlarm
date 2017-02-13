package com.gurungsijan.syncalarm.database;

import com.gurungsijan.syncalarm.repository.Alarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sijan Gurung on 06/02/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */
public class DbHelper {
    private static DbHelper ourInstance = new DbHelper();

    public static DbHelper getInstance() {
        return ourInstance;
    }

    private DbHelper() {
    }


    public List<Alarm> getAllAlarms(){
        List<Alarm> alarms = new ArrayList<Alarm>();
        return Collections.emptyList();
    }
}
