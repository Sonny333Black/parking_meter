package com.example.androidstudio.hfragDebugLog;
import android.app.Fragment;
//import android.util.Log;
import fllog.Log;

import java.text.SimpleDateFormat;

public class Controller extends StateMachine {
    private static final String TAG = "sonny-Controller";

    private OnControllerInteractionListener mListener;

    private int mEuroSum;
    private static final int MAX_SUM = 4;

    public enum SmMessage {
        INIT, ONE_EURO, TWO_EURO, OK, CANCEL, TIMER5S, TIMER2S, TIMER2SRE, TIMER30S
    }

    private enum State {
        START, WAIT_FOR_FIRST_EURO, WAIT_FOR_NEXT_EURO, THANKS
    }

    private State state = State.START;

    public static SmMessage[] messageIndex = SmMessage.values();

    public Controller() {
        Log.d(TAG, "Controller()");
    }

    public void init(Fragment frag) {
        Log.d(TAG, "init()");

        // init InterfaceListener
        try {
            mListener = (OnControllerInteractionListener) frag;
        } catch (ClassCastException e) {
            throw new ClassCastException(frag.toString()
                    + " must implement OnFragmentInteractionListener!");
        }

        // send message for start transition
        sendSmMessage(SmMessage.INIT.ordinal(), 0, 0, null);
    }

    @Override
    void theBrain(android.os.Message message) {
        SmMessage inputSmMessage = messageIndex[message.what];

        Log.i(TAG, "SM: state: " + state + ", input message: " +
                inputSmMessage.toString() + ", arg1: " +
                message.arg1 + ", arg2: " + message.arg2);
        if (message.obj != null) {
            Log.i(TAG, "SM: data: " + message.obj.toString());
        }

        switch (state) {
            case START:
                switch (inputSmMessage) {
                    case INIT:
                        Log.v(TAG, "in Init");
                        mEuroSum = 0;
                        mListener.onControllerDisplay("please pay!");
                        mListener.onControllerOutput("");

                        setTimer(SmMessage.TIMER2S.ordinal(), 0);
                        state = State.WAIT_FOR_FIRST_EURO;
                        break;
                    default:
                        Log.i(TAG, "SM: not a valid input in this state!");
                        break;
                }
                break;
            case WAIT_FOR_FIRST_EURO:
                switch (inputSmMessage) {
                    case TIMER2S:
                        setTimer(SmMessage.TIMER2SRE.ordinal(), 2000);
                        mListener.onControllerOutput("");
                        mListener.onControllerDisplay("please pay!");
                        break;
                    case TIMER2SRE:
                        setTimer(SmMessage.TIMER2S.ordinal(), 2000);
                        mListener.onControllerOutput("");
                        mListener.onControllerDisplay("now !!!!");
                        break;
                    case ONE_EURO:
                        mEuroSum = 1;
                        Log.i(TAG, "mEuroSum: " + mEuroSum);

                        mListener.onControllerDisplay("parking duration: 1 hour");

                        stopTimer(SmMessage.TIMER30S.ordinal());
                        setTimer(SmMessage.TIMER30S.ordinal(), 10000);
                        state = State.WAIT_FOR_NEXT_EURO;
                        break;
                    case TWO_EURO:
                        mEuroSum = 2;
                        Log.i(TAG, "mEuroSum: " + mEuroSum);

                        mListener.onControllerDisplay("parking duration: 2 hour");

                        stopTimer(SmMessage.TIMER30S.ordinal());
                        setTimer(SmMessage.TIMER30S.ordinal(), 10000);
                        state = State.WAIT_FOR_NEXT_EURO;
                        break;
                    default:
                        Log.i(TAG, "SM: not a valid input in this state!");
                        break;
                }
                break;
            case WAIT_FOR_NEXT_EURO:
                switch (inputSmMessage) {
                    case TIMER30S:
                        mListener.onControllerOutput("");
                        mListener.onControllerDisplay("please pay!");

                        setTimer(SmMessage.TIMER2S.ordinal(), 2000);
                        state = State.WAIT_FOR_FIRST_EURO;
                        break;
                    case ONE_EURO:
                        mEuroSum = mEuroSum + 1;
                        Log.i(TAG, "mEuroSum: " + mEuroSum);

                        if (mEuroSum == MAX_SUM) {
                            printTicket("maximum duration");
                            setTimer(SmMessage.TIMER5S.ordinal(), 5000);

                            state = State.THANKS;
                            break;
                        }
                        mListener.onControllerDisplay("parking duration: " + mEuroSum + " hour");

                        stopTimer(SmMessage.TIMER30S.ordinal());
                        setTimer(SmMessage.TIMER30S.ordinal(), 10000);
                        state = State.WAIT_FOR_NEXT_EURO;
                        break;
                    case TWO_EURO:
                        mEuroSum = mEuroSum + 2;
                        Log.i(TAG, "mEuroSum: " + mEuroSum);

                        if (mEuroSum >= MAX_SUM) {
                            //mEuroSum = MAX_SUM;
                            printTicket("maximum duration");
                            setTimer(SmMessage.TIMER5S.ordinal(), 5000);

                            state = State.THANKS;
                            break;
                        }
                        mListener.onControllerDisplay("parking duration: " + mEuroSum + " hour");

                        stopTimer(SmMessage.TIMER30S.ordinal());
                        setTimer(SmMessage.TIMER30S.ordinal(), 10000);
                        state = State.WAIT_FOR_NEXT_EURO;
                        break;
                    case OK:
                        PrintData data = (PrintData) message.obj;
                        String str = data.getText();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(data.getDate());
                        printTicket(str + timeStamp);
                        setTimer(SmMessage.TIMER5S.ordinal(), 5000);

                        state = State.THANKS;
                        break;
                    case CANCEL:
                        cancel();
                        setTimer(SmMessage.TIMER2S.ordinal(), 2000);
                        state = State.WAIT_FOR_FIRST_EURO;
                        break;
                    default:
                        Log.i(TAG, "SM: not a valid input in this state!");
                        break;
                }
                break;
            case THANKS:
                switch (inputSmMessage) {
                    case INIT:
                        break;
                    case ONE_EURO:
                    case TWO_EURO:
                    case OK:
                    case CANCEL:
                        stopTimer(SmMessage.TIMER5S.ordinal());
                        stopTimer(SmMessage.TIMER2S.ordinal());
                        stopTimer(SmMessage.TIMER2SRE.ordinal());
                        mListener.onControllerOutput("");
                        mListener.onControllerDisplay("please pay!");

                        state = State.WAIT_FOR_FIRST_EURO;
                        break;
                    case TIMER5S:
                        mListener.onControllerOutput("");
                        mListener.onControllerDisplay("please pay!");

                        state = State.WAIT_FOR_FIRST_EURO;
                        break;
                    default:
                        Log.i(TAG, "SM: not a valid input in this state!");
                        break;
                }
        }
        Log.i(TAG, "CURRENT STATE: " + state);
    }

    private void printTicket(String str) {
        if(mEuroSum == 5){
            mListener.onControllerOutput(str + "\nticket: 4 hour \n1 Euro back");
            mListener.onControllerDisplay("thank you for paying too much !!!!");
        }else{
            mListener.onControllerOutput(str + "\nticket: " + mEuroSum +" hour");
            mListener.onControllerDisplay("thank you for paying so much!!!!");
        }
        mEuroSum = 0;
    }

    private void cancel() {
        mListener.onControllerOutput("money back");
        mListener.onControllerDisplay("please pay!");
        mEuroSum = 0;
    }

    public interface OnControllerInteractionListener {
        public void onControllerDisplay(String str);

        public void onControllerOutput(String str);
    }
}
