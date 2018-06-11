package com.example.wuzelong.testvideodemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.xmwsh.videolib.media.IScreenSwitchingListener;
import com.xmwsh.videolib.media.PlayerView;

import java.security.acl.Group;

public class TestVideo extends AppCompatActivity implements IScreenSwitchingListener {

    private PlayerView mPlayerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        FrameLayout contentView = findViewById(R.id.contentView);
        WebView webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("http://www.baidu.com");
        contentView.addView(webview);
        init();
    }

    private void init() {
        mPlayerView = new PlayerView(this);
        mPlayerView.setUrl("rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov");
        FrameLayout.LayoutParams params = new
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,500);
        params.topMargin = 200;
        mPlayerView.setLayoutParams(params);
        mPlayerView.setScreenSwitchingListener(this);
        ViewGroup group = getWindow().getDecorView().findViewById(android.R.id.content);
        group.addView(mPlayerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayerView != null) {
            mPlayerView.starPlay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerView != null) {
            mPlayerView.stop();
        }
    }

    @Override
    public void onScreenSwitch(int direction) {
        WindowManager wm = getWindow().getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        if (direction == 1) {
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                Log.e("test","竖屏屏 width : "+width+" height : "+height);
                mPlayerView.setSize(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
            }
        }else {
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                Log.e("test","横屏 width : "+width+" height : "+height);
                mPlayerView.setSize(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
            }
        }
    }
}

