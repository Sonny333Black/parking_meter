package com.example.androidstudio.hfragDebugLog;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Observable;

import fllog.Log;

/**
 * Modelklasse
 */

public class Model extends Observable {     //!!a MVC3 !

    private static final String TAG = "fhflModel";
    private static final String SHARED_PREFERENCES_TAG = "Model";
    private static final String SHARED_PREFERENCES_CONTENT = "content";

    private int todoDone = 0;

    public void incTodoDone() {
        Log.d(TAG, "incTodoDone(): ObserverCount: " + countObservers() );

        todoDone++;
        setChanged();           //!!a MVC9 !
        notifyObservers();
    }

    public void reset() {
        Log.d(TAG, "reset()");

        todoDone = 0;
        setChanged();
        notifyObservers();
    }

    public int getTodoDone() {
        Log.d(TAG, "getTodoDone(): " + todoDone);
        return todoDone;
    }

    public void load(Activity activity) {            //!!a MVC2 !

        SharedPreferences sp = activity.getSharedPreferences(SHARED_PREFERENCES_TAG, 0);
        todoDone = sp.getInt(SHARED_PREFERENCES_CONTENT, 0);
    }

    public void save(Activity activity) {

        SharedPreferences sp = activity.getSharedPreferences(SHARED_PREFERENCES_TAG, 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(SHARED_PREFERENCES_CONTENT, todoDone );
        ed.commit();
    }
}
