/**
 * Basic Implementation von Fragments, Teil 5: Dynamische, geschachtelte Fragments + vollständiges
 *     Log-Fragment
 *
 * Geschachtelte Fragments:
 *     Todos- und Detail-Fragment (Code + layout.xml) aus den vorherigen Projekten
 *         erweitert um Persistenz und Zugriff als Observer auf das Model (Observable)
 *     Das ToDo-Fragment und das Log-Fragment erscheinen gleichzeitig
 *     Ziel ist, die vorhandenen Layout-Files ohne Aenderung einzusetzen. Da eine dynamische
 *         Einbindung verwendet/demonstriert werden soll (eine statische waere hier deutlich
 *         einfacher ).
 *
 * Log-Fragment
 *     bildet die Basis fuer alle folgenden Projekte, in denen Apps debuggt werden muessen, ohne
 *         via USB an den rechner angebunden zu sein (z.B. GPS-Apps oder NFC / Bluetooth-Apps)
 *     ersetzt android.util.Log
 *     das Log-Fragment wird immer dynamisch eingebunden -> hohe Flexibilitaet
 *
 * Zugunsten der Uebersichtlickeit wurde auf eine Backstack-Unterstützung verzichtet.
 *
 * Android-Techniken:
 *     Ueberschreibung von android.util.Log
 *     gechachtelte dynamische Fragments
 *     vollständige Persistenz
 *     Observer/Observable Klasse
 *     verzoegerte Initialisierung von UI-Elementen via post(runnable..)
 *
 * Todos:
 *
 * Programmier-Doku:
 *     Source ist selbsterklaerend
 *
 * Voraussetzungen:
 *     API Level 15 -> android.support.v7.app.AppCompatActivity muss eingebunden werden
 *          da das Context-Menue verwendet wird
 *
 * History:
 *     16.09.15 tas Start der Programmierung
 *     27.11.15 tas
 *     30.11.15 tas Version 1.0
 */

package com.example.androidstudio.hfragDebugLog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import fllog.Log;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "fhflMainActivity";

    private static final String SHARED_PREFERENCES_TAG = "MainActivity";
    private static final String SHARED_PREFERENCES_ACTIVE_FRAG = "frame";

    private String statusView = "";
    private MainActivityFragment mParkFrag;
    private TwoPaneFragment mTwoPaneFrag;
    private boolean mLogFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.init(true, true);
        Log.d(TAG, "onCreate(): LogFragment initialisiert");

        SharedPreferences sp = getSharedPreferences(SHARED_PREFERENCES_TAG, 0);
        statusView = sp.getString(SHARED_PREFERENCES_ACTIVE_FRAG, "Shared Preferences nicht initialisiert\n");

        switch(statusView){
            case "showPark":
                showPark();
                break;
            case "showLog":
                showLog();
                break;
            case "showTwoPane":
                showTwoPane();
                break;
            default:
                showLog();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected( " + item.toString() + " )");

        int id = item.getItemId();

        switch (id) {
            case R.id.action_parkAndLog:
                showTwoPane();
                return true;
            case R.id.action_parkschein:
                showPark();
                return true;
            case R.id.action_logAusgabe:
                showLog();
                return true;
            default:
                Log.d(TAG, "ERROR :)");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPark() {
        removeAllFrags();
        statusView ="showPark";
        Log.d(TAG, "showPark()");

        mParkFrag = new MainActivityFragment();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment_container, mParkFrag, "MAIN")
                .commit();

        getFragmentManager().executePendingTransactions();
    }

    private void showLog() {
        removeAllFrags();
        statusView = "showLog";
        Log.d(TAG, "showLog()");

        mLogFrag = true;

        getFragmentManager()
                .beginTransaction()
                .add(R.id.main_fragment_container, Log.getFragment())
                .commit();

        getFragmentManager().executePendingTransactions();
    }

    private void showTwoPane() {
        removeAllFrags();
        statusView = "showTwoPane";
        Log.d(TAG, "showTwoPane()");

        mTwoPaneFrag = TwoPaneFragment.newInstance("", "");
        mParkFrag = new MainActivityFragment();
        mLogFrag = true;

        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, mTwoPaneFrag, "TWO_PANE")
                .add(R.id.todo_fragment_container, mParkFrag)
                .add(R.id.log_fragment_container, Log.getFragment())
                .commit();

        getFragmentManager().executePendingTransactions();
    }

    private void removeAllFrags() {
        Log.d(TAG, "removeAllFrags():");
        if (mParkFrag != null)
            getFragmentManager()
                    .beginTransaction()
                    .remove(mParkFrag)
                    .commit();
        if (mLogFrag)
            getFragmentManager()
                    .beginTransaction()
                    .remove(Log.getFragment())
                    .commit();
        if (mTwoPaneFrag != null)
            getFragmentManager()
                    .beginTransaction()
                    .remove(mTwoPaneFrag)
                    .commit();

        mParkFrag = null;
        mTwoPaneFrag = null;
        mLogFrag = false;

        getFragmentManager().executePendingTransactions();
    }

    @Override
    public void onPause() {
        super.onPause();
        android.util.Log.d(TAG, "onPause()");
        removeAllFrags();

        SharedPreferences sp = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_TAG, 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(SHARED_PREFERENCES_ACTIVE_FRAG, statusView);
        ed.commit();
    }
}
