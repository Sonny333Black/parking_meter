package com.example.androidstudio.hfragDebugLog;
import java.util.Date;


public class PrintData {
    private String mText;
    private Date mDate;

    public PrintData(String text, Date d) {
        mText = text;
        mDate = d;
    }
    public String getText(){
        return mText;
    }
    public Date getDate(){
        return mDate;
    }
}
