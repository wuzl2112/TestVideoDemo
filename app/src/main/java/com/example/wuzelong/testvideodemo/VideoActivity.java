package com.example.wuzelong.testvideodemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xmwsh.videolib.media.PlayerView;
import com.xmwsh.videolib.media.IScreenSwitchingListener;

public class VideoActivity extends AppCompatActivity implements IScreenSwitchingListener {

    public static final String VIDEO_URL_KEY = "videoPath";

    private PlayerView mPlayerView;
    private ImageView ivShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_video);
        ivShot = findViewById(R.id.ivShot);
        mPlayerView = findViewById(R.id.playerView);
        mPlayerView.setClickCanShowBottomView(false);
        mPlayerView.setBottomViewBg(Color.TRANSPARENT);

        mPlayerView.setScreenSwitchingListener(this);
        String videoPath = getIntent().getStringExtra(VIDEO_URL_KEY);
        mPlayerView.setUrl(videoPath);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mPlayerView.resume();
        mPlayerView.starPlay();
        Log.e("test","onResume ... ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.pause();
        Log.e("test","onPause ... ");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("test","onStop ... ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("test","onDestroy ... ");
        mPlayerView.stop();
    }

    @Override
    public void onScreenSwitch(int direction) {
        if (direction == 1) {
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
                mPlayerView.setShrinkIcon(ContextCompat.getDrawable(this,R.drawable.ic_icon_all_screen));
            }
        }else {
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
                mPlayerView.setShrinkIcon(ContextCompat.getDrawable(this,R.drawable.ic_icon_quit_all_screen));
            }
        }
    }

    public void onScreenShotListener(View v) {
        String img = mPlayerView.shotScreen();
        if (!TextUtils.isEmpty(img)) {
            Bitmap bmp = BitmapFactory.decodeFile(img);
            ivShot.setImageBitmap(bmp);
        }
    }

}
