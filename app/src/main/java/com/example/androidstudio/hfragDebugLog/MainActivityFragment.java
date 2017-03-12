package com.example.androidstudio.hfragDebugLog;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */

public class MainActivityFragment extends Fragment implements View.OnClickListener,
        Controller.OnControllerInteractionListener, Observer {

    private static final String    TAG    = "sonny-Parkscheinautomat";
    private static final String SHARED_PREFERENCES_TAG = "ParkFragment";
    private static final String SHARED_PREFERENCES_VIEW = "viewContent";
    private static final String SHARED_PREFERENCES_EURO= "euro";
    private static final String SHARED_PREFERENCES_TEXT_VIEW = "textView";

    private Controller controller;
    private Button butOneEuro;
    private Button butTwoEuro;
    private Button butOk;
    private Button butCancel;
    private TextView tvDisplay;
    private TextView tvOutput;

    public MainActivityFragment() {
        int a;
        a = 5;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //mModel = ((MainActivity) getActivity()).model;
        //android.util.Log.d(TAG, "onCreateView(): lade SharedPreferences");
        //SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREFERENCES_TAG, 0);
        //String strTextView = sp.getString(SHARED_PREFERENCES_TEXT_VIEW, "Details Todo 1\n");

        controller = new Controller();
        controller.init(this);  // also init for OnControllerInteractionListener

        butOneEuro = (Button) view.findViewById(R.id.butOneEuro);
        butOneEuro.setOnClickListener(this);
        butTwoEuro = (Button) view.findViewById(R.id.butTwoEuro);
        butTwoEuro.setOnClickListener(this);

        butOk = (Button) view.findViewById(R.id.butOk);
        butOk.setOnClickListener(this);
        butCancel = (Button) view.findViewById(R.id.butCancel);
        butCancel.setOnClickListener(this);
        tvDisplay = (TextView) view.findViewById(R.id.tvDisplay);
        tvOutput = (TextView) view.findViewById(R.id.tvOutput);

        return view;
    }

    // @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butOneEuro:
                Log.d(TAG, "onClick(): Button OneEuro");
                controller.sendSmMessage(Controller.SmMessage.ONE_EURO.ordinal(), 0, 0, null);
                break;
            case R.id.butTwoEuro:
                Log.d(TAG, "onClick(): Button TwoEuro");
                controller.sendSmMessage(Controller.SmMessage.TWO_EURO.ordinal(), 0, 0, null);
                break;
            case R.id.butOk:
                Log.d(TAG, "onClick(): Button Ok");
                PrintData extraData = new PrintData("PrintDate: ", new Date());
                controller.sendSmMessage(Controller.SmMessage.OK.ordinal(), 0, 0, extraData);
                break;
            case R.id.butCancel:
                Log.d(TAG, "onClick(): Button Cancel");
                controller.sendSmMessage(Controller.SmMessage.CANCEL.ordinal(), 0, 0, null);
                break;
        }
    }

    public void onControllerDisplay(String str){
        Log.d(TAG, "onControllerDisplay(): " + str);
        tvDisplay.setText(str);
    }

    public void onControllerOutput(String str){
        Log.d(TAG, "onControllerOutput(): " + str);
        tvOutput.setText(str);
    }

    @Override
    public void update(Observable observable, Object o) {
    }
}
