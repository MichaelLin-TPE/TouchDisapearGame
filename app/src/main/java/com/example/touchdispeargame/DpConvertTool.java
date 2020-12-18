package com.example.touchdispeargame;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DpConvertTool {
    private static DpConvertTool instance = null;

    public static DpConvertTool getInstance(){
        if (instance == null){
            instance = new DpConvertTool();
            return instance;
        }
        return instance;
    }

    public int convertDb(){
        Context context = MyApplication.getInstance().getApplicationContext();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        //取得螢幕的寬
        float width = (float) context.getResources().getDisplayMetrics().widthPixels;
        float singleItemSize = (float) width / 7 / metrics.density;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,singleItemSize,context.getResources().getDisplayMetrics());
    }

    public int getScreenSize(){
        return (int) MyApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }
    public int getDb(int pix){

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,pix,MyApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics());
    }

}
