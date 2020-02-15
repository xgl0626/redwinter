package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class wleomeActivity extends AppCompatActivity {
    private ImageView welcome;
    @SuppressLint("HandlerLeak")
    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //界面转载
            Intent intent = new Intent(wleomeActivity.this,LoginActivity.class);
            startActivities(new Intent[]{intent});  //start跳转
            finish();//结束欢迎界面活动
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        welcome=(ImageView)findViewById(R.id.welcome);
        ActionBar actionBar = getSupportActionBar();     //取消标题头actionbar
        if (actionBar != null) {
            actionBar.hide();
        }
        //延迟发送信息2000Ms即2秒
        handler.sendMessageDelayed(Message.obtain(), 2000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
