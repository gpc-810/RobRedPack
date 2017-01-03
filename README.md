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
   进入之后，扫描当前布局
