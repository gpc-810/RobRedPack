package com.gpc.robredpack;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class FloatServices extends Service {
    private Button mButton;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow2();

        return super.onStartCommand(intent, flags, startId);
    }


    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    mLayoutParams.x = mLayoutParams.x + movedX;
                    mLayoutParams.y = mLayoutParams.y + movedY;
                    Log.e("onTouch: ", mLayoutParams.x + "    " + mLayoutParams.y);

                    // 更新悬浮窗控件布局
                    mWindowManager.updateViewLayout(v, mLayoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }


    /**
     * 悬浮窗口 视屏
     */
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {

            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View displayView = layoutInflater.inflate(R.layout.image_display, null);
            displayView.setOnTouchListener(new FloatingOnTouchListener());

            // 获取WindowManager服务
            final MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            SurfaceView surfaceView = displayView.findViewById(R.id.video_display_surfaceview);
            final SurfaceHolder surfaceHolder = surfaceView.getHolder();


            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mediaPlayer.setDisplay(surfaceHolder);
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                }

            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });


            try {
                mediaPlayer.setDataSource(this, Uri.parse("https://raw.githubusercontent" + "" +
                        ".com/dongzhong/ImageAndVideoStore/master/Bruno%20Mars%20-%20Treasure.mp4"));
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Toast.makeText(this, "无法打开视频源", Toast.LENGTH_LONG).show();
            }


            // 设置LayoutParam
            mLayoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            //　flag参数必须设置 不然有悬浮窗 但其他应用也无法使用
            mLayoutParams.flags = mLayoutParams.FLAG_NOT_TOUCH_MODAL | mLayoutParams.FLAG_NOT_FOCUSABLE | mLayoutParams.FLAG_FULLSCREEN |
                    mLayoutParams.FLAG_LAYOUT_IN_SCREEN;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.width = 300;
            mLayoutParams.height = 300;
            mLayoutParams.x = 300;
            mLayoutParams.y = 300;

            // 将悬浮窗控件添加到WindowManager
            mWindowManager.addView(displayView, mLayoutParams);
        }
    }


    /**
     * 悬浮窗口Imageview
     */
    private void showFloatingWindow2() {
        if (Settings.canDrawOverlays(this)) {
            // 获取WindowManager服务
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            //            LayoutInflater layoutInflater = LayoutInflater.from(this);
            MyImageView displayView = new MyImageView(this);
            displayView.setOnTouchListener(new FloatingOnTouchListener());
            //            ImageView imageView = displayView.findViewById(R.id.image_display_imageview);
            displayView.setImageResource(android.R.drawable.screen_background_dark_transparent);
            //            displayView.


            // 设置LayoutParam
            mLayoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            //　flag参数必须设置 不然有悬浮窗 但其他应用也无法使用
            mLayoutParams.flags = mLayoutParams.FLAG_NOT_TOUCH_MODAL | mLayoutParams.FLAG_NOT_FOCUSABLE | mLayoutParams.FLAG_FULLSCREEN |
                    mLayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.width = 100;
            mLayoutParams.height = 100;
            mLayoutParams.x = -10;
            mLayoutParams.y = 258;

            //             将悬浮窗控件添加到WindowManager
            mWindowManager.addView(displayView, mLayoutParams);
        }
    }

    /**
     * 悬浮窗口button
     */
    private void showFloatingWindow1() {
        if (Settings.canDrawOverlays(this)) {
            // 获取WindowManager服务
            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            // 新建悬浮窗控件
            mButton = new Button(getApplicationContext());
            mButton.setText("Floating Window");
            mButton.setBackgroundColor(Color.BLUE);

            // 设置LayoutParam
            mLayoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            //　flag参数必须设置 不然有悬浮窗 但其他应用也无法使用
            mLayoutParams.flags = mLayoutParams.FLAG_NOT_TOUCH_MODAL | mLayoutParams.FLAG_NOT_FOCUSABLE | mLayoutParams.FLAG_FULLSCREEN |
                    mLayoutParams.FLAG_LAYOUT_IN_SCREEN;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.width = 500;
            mLayoutParams.height = 100;
            mLayoutParams.x = 300;
            mLayoutParams.y = 300;

            // 将悬浮窗控件添加到WindowManager
            mWindowManager.addView(mButton, mLayoutParams);
        }
    }
}
