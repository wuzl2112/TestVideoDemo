package com.xmwsh.videolib.media;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.xmwsh.videolib.R;


import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerView extends FrameLayout
        implements View.OnClickListener{

    private String mUrl;
    private IjkVideoView mVideoView;
    private View videSetView;
//    private TextView tvShowInfo;
    private TextView tvSetting;
    private ImageView ivShrink;
    private TableLayout mHudView;
    private RelativeLayout bottomView;
    private ShowBottomViewHandler mShowBottomViewHandler;
    private int degreee = 0;
    private IScreenSwitchingListener mIScreenSwitchingListener;
    private boolean isClickCanShowBottomView = true;
    private boolean isClickCanShowPlayInfoView = true;
    private TextView tvRotation;


    public PlayerView(@NonNull Context context) {
        this(context,null);
    }

    public PlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.video_play_control,this);
        tvSetting = findViewById(R.id.tvSetting);
        ivShrink = findViewById(R.id.ivShrink);
        ivShrink.setTag(1);
        videSetView = findViewById(R.id.videSetView);
        bottomView = findViewById(R.id.bottomView);
        TextView tv16scan9 = findViewById(R.id.tv16scan9);
        TextView tv4scan3 = findViewById(R.id.tv4scan3);
        TextView tvfit = findViewById(R.id.tvfit);
        tvRotation = findViewById(R.id.tvRotation);
        TextView tvScreenshot = findViewById(R.id.tvScreenshot);
        initPlayer();
        tvSetting.setOnClickListener(this);
        tvfit.setOnClickListener(this);
        tv16scan9.setOnClickListener(this);
        tv4scan3.setOnClickListener(this);
        ivShrink.setOnClickListener(this);
        tvRotation.setOnClickListener(this);
        tvScreenshot.setOnClickListener(this);
        mShowBottomViewHandler = new ShowBottomViewHandler();
    }

    private void initPlayer() {
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView = findViewById(R.id.ijkVideoView);
        mHudView = findViewById(R.id.hud_view);
        mVideoView.setHudView(mHudView);
        mHudView.setOnClickListener(this);
        int scan = MeasureScanSp.getVideoScan(getContext());
        switch (scan) {
            case 0 :
                tvSetting.setText("自适应");
                break;
            case 4:
                tvSetting.setText("16:9");
                break;
            case 5:
                tvSetting.setText("4:3");
                break;
        }

    }


    long oldTime = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (isClickCanShowPlayInfoView) {
                long time = System.currentTimeMillis();
                if (time - oldTime < 1000) {
                    mHudView.setVisibility(View.VISIBLE);
                }else {
                    oldTime = System.currentTimeMillis();
                }
            }

            if (isClickCanShowBottomView) {
                bottomView.setVisibility(View.VISIBLE);
                if (mShowBottomViewHandler != null) {
                    mShowBottomViewHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mShowBottomViewHandler.sendEmptyMessage(1);
                        }
                    },3500);
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvSetting) {
            videSetView.setVisibility(VISIBLE);
        }
        else if (i == R.id.hud_view) {//隐藏视频信息
            mHudView.setVisibility(View.GONE);
        }
        else if (i == R.id.tvfit) {//自适应
            mVideoView.toggleAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
            tvSetting.setText("自适应");

        } else if (i == R.id.tv16scan9) {//16：9
            mVideoView.toggleAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
            tvSetting.setText("16:9");

        } else if (i == R.id.tv4scan3) {//4：3
            mVideoView.toggleAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
            tvSetting.setText("4:3");

        } else if (i == R.id.ivShrink) {//横竖屏切换
            if (mIScreenSwitchingListener != null) {
                int tagSc = (int) ivShrink.getTag();
                if (tagSc == 1) {
                    mIScreenSwitchingListener.onScreenSwitch(2);
                    ivShrink.setTag(2);
                } else {
                    mIScreenSwitchingListener.onScreenSwitch(1);
                    ivShrink.setTag(1);
                }
            }

        } else if (i == R.id.tvRotation) {//旋转
            degreee += 90;
            mVideoView.setPlayVideoRotation(degreee);
        }
    }



    class ShowBottomViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            videSetView.setVisibility(GONE);
            bottomView.setVisibility(View.GONE);
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (!mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    /**
     * 暂停
     */
    public void pause() {
        if (!mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.pause();
        }
    }


    public void resume() {
        mVideoView.resume();
    }

    /**
     * 设置播放链接
     * @param url
     */
    public void setUrl(String url) {
        this.mUrl = url;
        mVideoView.setVideoPath(mUrl);
    }

    /**
     * 开始播放
     */
    public void starPlay() {
        mVideoView.start();
    }

    /**
     * 截屏
     * @return
     */
    public String shotScreen() {
        return mVideoView.getScreenShot();
    }

    /**
     * 设置全屏播放监听
     * @param listener
     */
    public void setScreenSwitchingListener(IScreenSwitchingListener listener) {
        this.mIScreenSwitchingListener = listener;
    }

    /**
     * 是否可以点击显示底部
     * @param enable
     */
    public void setClickCanShowBottomView(boolean enable) {
        this.isClickCanShowBottomView = enable;
    }

    /**
     * 是否可点击显示视频信息
     * @param enable
     */
    public void setClickCanShowPlayInfoView(boolean enable) {
        this.isClickCanShowPlayInfoView = enable;
    }

    /**
     * 设置video大小
     * @param width
     * @param height
     */
    public void setSize(int width,int height) {
        LayoutParams params = new LayoutParams(width,height);
        setLayoutParams(params);
    }

    /**
     * 底部背景
     * @param res
     */
    public void setBottomViewBg(int res) {
        if (bottomView != null) {
            bottomView.setVisibility(VISIBLE);
            bottomView.setBackgroundColor(res);
        }
    }

    /**
     * 设置底部背景
     * @param drawable
     */
    public void setBottomViewBg(Drawable drawable) {
        if (bottomView != null && drawable != null) {
            bottomView.setVisibility(VISIBLE);
            bottomView.setBackground(drawable);
        }
    }

    /**
     * 是否显示底部
     * @param isShow
     */
    public void setShowBottomView(boolean isShow) {
        if (bottomView != null ) {
            bottomView.setVisibility(isShow ? VISIBLE : GONE);
        }
    }

    /**
     * 修改全屏和退出全屏图标
     * @param drawable
     */
    public void setShrinkIcon(Drawable drawable) {
        if (ivShrink != null && drawable != null) {
            ivShrink.setImageDrawable(drawable);
        }
    }

}
