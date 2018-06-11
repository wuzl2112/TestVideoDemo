package com.example.wuzelong.testvideodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;


import com.example.wuzelong.testvideodemo.zxing.activity.CaptureActivity;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {

    private EditText etVideoPath;
    private static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etVideoPath = findViewById(R.id.etVideoPath);
    }

    public void onRtspListener(View v) {
        Intent intent = new Intent(this,VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEO_URL_KEY,"rtsp://admin:wnvxiaoti5566123@10.17.5.99:554/Streaming/Channels/101");
        startActivity(intent);
    }

    public void onRtmpListener(View v) {
        Intent intent = new Intent(this,VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEO_URL_KEY,"rtsp://admin:wnvxiaoti5566123@59.61.78.162:5555/Streaming/Channels/102");
        startActivity(intent);
    }

    public void onHttpListener(View v) {
        Intent intent = new Intent(this,VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEO_URL_KEY,"rtmp://47.96.77.62:10935/hls/stream_1");
        startActivity(intent);
    }

    public void onQcodeListener(View v) {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onPlayListener(View v) {
        String videoPath = etVideoPath.getText().toString();
        if (!TextUtils.isEmpty(videoPath)) {
            Intent intent = new Intent(this,VideoActivity.class);
            intent.putExtra(VideoActivity.VIDEO_URL_KEY,videoPath);
            startActivity(intent);
        }else {
            Toast.makeText(this,"请输入视频链接",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("test","onActivityResult requestCode : "+requestCode + " resultCode : "+resultCode + " data : "+data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                if (data != null) {
                    String result = data.getStringExtra(CaptureActivity.SCAN_RESULT_KEY);
                    Log.e("test","result : "+result);
                    etVideoPath.setText(result);
                }
            }
        }
    }
}
