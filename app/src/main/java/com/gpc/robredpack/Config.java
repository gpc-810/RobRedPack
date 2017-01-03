package com.gpc.robredpack;

/**
 * @author 郭鹏超
 */
public class Config {

    public static final String ACTION_QIANGHONGBAO_SERVICE_DISCONNECT = "com.gpc.robredpack.ACCESSBILITY_DISCONNECT";//断开
    public static final String ACTION_QIANGHONGBAO_SERVICE_CONNECT = "com.gpc.robredpack.ACCESSBILITY_CONNECT";//开启
//    public static final String ACTION_QIANGHONGBAO_NOTIFICATION = "com.gpc.robredpack.ACCESSBILITY_NOTIFICATION";
//    public static final String PREFERENCE_NAME = "config";
//    public static final String KEY_ENABLE_WECHAT = "KEY_ENABLE_WECHAT";
//    public static final String KEY_WECHAT_AFTER_OPEN_HONGBAO = "KEY_WECHAT_AFTER_OPEN_HONGBAO";
//    public static final String KEY_WECHAT_DELAY_TIME = "KEY_WECHAT_DELAY_TIME";

    public static final int WX_AFTER_OPEN_HONGBAO = 0;
    public static final int WX_AFTER_OPEN_SEE = 1; //看大家手气
    public static final String WX_HONGBAO_STRING = "微信红包"; //微信红包
    public static final String QQ_HONGBAO_STRING = "QQ红包"; //微信红包

    public static final int QQ = 1;//qq普通
    public static final int WX = 2;//微信红包
    public static final int QQ_ZONE = 3;//QQ空间红包
    public static final int QQ_CMD = 4;//QQ口令红包


//    SharedPreferences preferences;
//
//    public Config(Context context) {
//        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
//    }
//
//    /**
//     * 是否启动微信抢红包
//     */
//    public boolean isEnableWechat() {
//        return preferences.getBoolean(KEY_ENABLE_WECHAT, true);
//    }
//
//    /**
//     * 微信打开红包后的事件
//     */
//    public int getWechatAfterOpenHongBaoEvent() {
//        int defaultValue = 0;
//        String result = preferences.getString(KEY_WECHAT_AFTER_OPEN_HONGBAO, String.valueOf(defaultValue));
//        try {
//            return Integer.parseInt(result);
//        } catch (Exception e) {
//        }
//        return defaultValue;
//    }
//
//    /**
//     * 微信打开红包后延时时间
//     */
//    public int getWechatOpenDelayTime() {
//        int defaultValue = 0;
//        String result = preferences.getString(KEY_WECHAT_DELAY_TIME, String.valueOf(defaultValue));
//        try {
//            return Integer.parseInt(result);
//        } catch (Exception e) {
//        }
//        return defaultValue;
//    }

    /**
     * 设置哪个软件的通知
     * @param packageName
     */
    public static void setFlag(String packageName) {
        if (packageName.equals("com.tencent.mm")) {
            MyAccessibilityService.mFlag = Config.WX;
        } else if (packageName.equals("com.tencent.mobileqq")) {
            MyAccessibilityService.mFlag = Config.QQ;
        }
    }


}
