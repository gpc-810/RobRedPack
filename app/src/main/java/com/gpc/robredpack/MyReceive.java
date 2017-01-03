package com.gpc.robredpack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by GPC on 2016/12/29.
 * MyReceive
 */
public class MyReceive extends BroadcastReceiver  {
    private static final String TAG = "MyReceive";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, intent.getStringExtra("msg"));


    }

}
