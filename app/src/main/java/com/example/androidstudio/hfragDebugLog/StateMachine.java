package com.example.androidstudio.hfragDebugLog;
import android.os.Handler;

/**
 *
 */
public class StateMachine extends Handler {

    @Override
    public void handleMessage(android.os.Message message) {
        theBrain(message);
    }

    /**
     * virtual method, must be overwritten in subclass
     * @param message
     */
    void theBrain(android.os.Message message){
    }

    protected void setTimer(int messageType, long durationMs){
        android.os.Message msg = new android.os.Message();
        msg.what = messageType;
        msg.arg1 = 0;
        msg.arg2 = 0;
        sendMessageDelayed(msg, durationMs);
    }

    protected void stopTimer(int messageType){
        removeMessages(messageType);
    }

    public void sendSmMessage(int messageType, int arg1, int arg2, Object obj){
        android.os.Message msg = new android.os.Message();
        msg.what = messageType;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        if ( obj != null ) {
            msg.obj = obj;
        }
        this.sendMessage(msg);
    }

}
