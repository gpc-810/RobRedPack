package com.gpc.robredpack;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by GPC on 2017/1/22.
 * SP_Util
 */

public class SP_Util {
    public static void saveSeelp(Context context, long time) {
        SharedPreferences sp = context.getSharedPreferences("time", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("seelp", time);
        editor.commit();
    }

    public static long getSeelp(Context context) {
        SharedPreferences sp = context.getSharedPreferences("time", Context.MODE_PRIVATE);
        return sp.getLong("seelp", 100L);

    }
}
