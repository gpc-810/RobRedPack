# RobRedPack
## 抢红包原理：辅助服务类MyAccessibilityService extends AccessibilityService  
    重写 onAccessibilityEvent方法，监听手机变化。
###1. 监听通知栏
     监听通知栏消息：AccessibilityEvent.getEventType==AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
      有消息 进入通知栏
                            Notification notification = (Notification) event.getParcelableData();
                            PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                                MyAccessibilityService.canGet=true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
###2.进入聊天界面：
   监听窗口变化：AccessibilityEvent.getEventType==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;//在这里面判断是否是进入了聊天界面
   进入之后，扫描当前布局寻找红包节点。
####红包节点查找方法：
#####1.
   ![Image text](https://raw.githubusercontent.com/gpc-810/RobRedPack/master/readme-image/android_tool.png)
#####2.
   ![Image text](https://raw.githubusercontent.com/gpc-810/RobRedPack/master/readme-image/android_dump.png)
#####3.
   ![Image text](https://raw.githubusercontent.com/gpc-810/RobRedPack/master/readme-image/weixinhongbao.png)




####找到最新的红包打开
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

###3.返回
    略。自己看代码吧

##备注
    以上只是讲解一下思路，具体逻辑实现请看代码。（返回首页延迟最好设置在200毫秒以上）（QQ空间红包未实现）
##说明
    该代码仅供参考学习，严禁用于商业目的。



