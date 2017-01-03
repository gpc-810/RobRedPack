package com.gpc.robredpack;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyAccessibilityService extends AccessibilityService {

    private static MyAccessibilityService service;


    public static boolean canGet = true;

    //窗口状态
    private static final int WINDOW_NONE = 0;
    private static final int WINDOW_LUCKYMONEY_RECEIVEUI = 1;
    private static final int WINDOW_LUCKYMONEY_DETAIL = 2;
    private static final int WINDOW_LAUNCHER = 3;
    private static final int WINDOW_OTHER = -1;
    //当前窗口
    private int mCurrentWindow = WINDOW_NONE;

    //当前通知
    public static int mFlag = Config.WX;
    /**
     * QQ口令发送
     */
    public static int QQ_SEND = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.e("demo", Integer.toString(eventType));

        switch (eventType) {
            //第一步：监听通知栏消息
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence text : texts) {
                        String content = text.toString();
                        Log.i("demo", "text:"+content);
                        if (content.contains(Config.WX_HONGBAO_STRING)||content.contains(Config.QQ_HONGBAO_STRING)) {
                            Notification notification = (Notification) event.getParcelableData();
                            PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                                MyAccessibilityService.canGet=true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: //是否进入了红包消息（聊天）界面
                String className = event.getClassName().toString();
                if (className.equals("com.tencent.mm.ui.LauncherUI") || className.equals("com.tencent.mobileqq.activity.SplashActivity")) {
                    mCurrentWindow = WINDOW_LAUNCHER;
                    //开始抢红包
                    Log.i("demo", "准备抢红包...");
                    getPacket();
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI")) {
                    mCurrentWindow = WINDOW_LUCKYMONEY_RECEIVEUI;
                    //开始打开红包
                    Log.i("demo", "打开红包");
                    openPacket();
                } else if (className.equals("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI")) {
                    mCurrentWindow = WINDOW_LUCKYMONEY_DETAIL;
                    //返回以方便下次收红包
                    Log.i("demo", "返回");
//                    performBack(this);
                } else {
                    mCurrentWindow = WINDOW_OTHER;
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED: //
                if (mCurrentWindow != WINDOW_LAUNCHER) { //不在聊天界面或聊天列表，不处理
//                    openNotify(event);
                    return;
                }
                if (canGet) {
                    getPacket();
                } else {
                    AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                    if (nodeInfo == null) {
                        return;
                    }
                    QQ_SEND = 0;
                    List<AccessibilityNodeInfo> listCMD = (nodeInfo.findAccessibilityNodeInfosByText("点击输入口令"));
                    redPakgType(nodeInfo, "点击输入口令", listCMD);
                }
                break;

            default:

                break;
        }


    }


    @Override
    public void onInterrupt() {

    }


    //找到红包并点击
    @SuppressLint("NewApi")
    private void getPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> listWX = (nodeInfo.findAccessibilityNodeInfosByText("微信红包"));
        List<AccessibilityNodeInfo> listQQ = (nodeInfo.findAccessibilityNodeInfosByText("QQ红包"));
        List<AccessibilityNodeInfo> listCMD = (nodeInfo.findAccessibilityNodeInfosByText("点击输入口令"));
        List<AccessibilityNodeInfo> list2 = (nodeInfo.findAccessibilityNodeInfosByText("QQ红包个性版"));
        if (redPakgType(nodeInfo, "[微信红包]", listWX)) {
            return;
        } else if (redPakgType(nodeInfo, "[QQ红包]", listQQ)) {
            return;
        }


    }

    /**
     * @param nodeInfo
     * @param pakgTypeString [微信红包]
     * @return
     */
    private boolean redPakgType(AccessibilityNodeInfo nodeInfo, String pakgTypeString, List<AccessibilityNodeInfo> list) {
        // 找到领取红包的点击事件
        if (list != null) {
            if (list.isEmpty()) {
                Log.i("demp", "领取列表为空");
                // 从消息列表查找微信红包
                AccessibilityNodeInfo nodeWX = findNodeInfosByText(nodeInfo, pakgTypeString);
                if (nodeWX != null) {
                    canGet = true;
                    performClick(nodeWX);
                } else {
                    return false;
                }

            } else {
                if (pakgTypeString.equals("点击输入口令") || canGet) {
                    AccessibilityNodeInfo node = list.get(list.size() - 1);
                    performClick(node);
                    Log.i("demo", "canGet=false");
                    canGet = false;
                }
            }
            return true;
        }
        return false;
    }


    private boolean isCmdRedPkg() {

        return true;

    }


    //打开红包
    @SuppressLint("NewApi")
    private void openPacket() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }

        Log.i("demo", "查找打开按钮...");
        AccessibilityNodeInfo targetNode = null;

        //如果红包已经被抢完则直接返回
        targetNode = findNodeInfosByText(nodeInfo, "看看大家的手气");
        if (targetNode != null) {
            performBack(this);
            return;
        }
        //通过组件名查找开红包按钮，还可通过组件id直接查找但需要知道id且id容易随版本更新而变化，旧版微信还可直接搜“開”字找到按钮
        if (targetNode == null) {
            Log.i("demo", "打开按钮中...");
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                AccessibilityNodeInfo node = nodeInfo.getChild(i);
                if ("android.widget.Button".equals(node.getClassName())) {
                    targetNode = node;
                    break;
                }
            }
        }
        //若查找到打开按钮则模拟点击
        if (targetNode != null) {
            final AccessibilityNodeInfo n = targetNode;
            performClick(n);
        }
    }


    //通过文本查找节点
    public AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    //模拟点击事件
    public void performClick(AccessibilityNodeInfo nodeInfo) {
        Config.setFlag(nodeInfo.getPackageName().toString());
        if (nodeInfo == null) {
            return;
        }
        switch (mFlag) {

            case Config.WX:
                if (nodeInfo.isClickable()) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else {
                    performClick(nodeInfo.getParent());
                }
                break;
            case Config.QQ:
                if (null == nodeInfo.getText() || nodeInfo.getText().toString().isEmpty()) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    AccessibilityNodeInfo nodeInfoSend = getRootInActiveWindow();
                    if (nodeInfoSend == null) {
                        return;
                    }
                    AccessibilityNodeInfo sendNode = findNodeInfosByText(nodeInfoSend, "发送");
                    if (null != sendNode) {
                        performClick(sendNode);
                    } else {
                        canGet = true;
                    }

                } else {
                    if ("发送".equals(nodeInfo.getText().toString())) {
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        canGet = true;
                    } else {
                        performClick(nodeInfo.getParent());
                    }
                }

                break;
            case Config.QQ_ZONE:
                if (nodeInfo.isClickable()) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else {
                    performClick(nodeInfo.getParent());
                }
                break;

            default:

                break;
        }


    }

    //模拟返回事件
    public void performBack(AccessibilityService service) {
        if (service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }


    /**
     * 判断当前服务是否正在运行
     * */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if(service == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if(info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if(i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if(!isConnect) {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "qianghongbao service destory");
        service = null;
        //发送广播，已经断开辅助服务
        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT);
        sendBroadcast(intent);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        service = this;
        //发送广播，已经连接上了
        Intent intent = new Intent(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT);
        sendBroadcast(intent);
        Toast.makeText(this, "已连接抢红包服务", Toast.LENGTH_SHORT).show();
    }
}
