package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.User;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.ToastUtils;

public class AnswerActivity extends photoActivity {
    private User mUser;
    private Toolbar toolbar;
    private EditText content;
    private Button back_top;
    private boolean isCommitting;
    private String mToken;
    private int mQid;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_activity);
        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mQid = intent.getIntExtra("qid", -1);
        content=(EditText)findViewById(R.id.answerContent);
        back_top=(Button)findViewById(R.id.back_up);
        back_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setUpActionBar();
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (isCommitting)
                    return true;
                switch(item.getItemId())
                {
                    case R.id.commit:upCommit();
                        break;
                    default:
                }
                return true;
            }
        });
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_commit, menu);
        return true;
    }
    private void upCommit() {
        if (isCommitting)
            return;
        String savecontent = content.getText().toString();
        if (savecontent.equals("")) {
            ToastUtils.showHint("内容不能为空");
            return;
        }
        isCommitting=true;
        HttpUtil.sendHttpRequest(apiconfig.POST_ANSWER, "qid="+mQid + "&content=" + savecontent + "&token=" + mToken, new HttpUtil.Callback() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                if(response.isSuccess())
                {
                    ToastUtils.showHint("上传成功");
                    finish();
                }
                else
                {
                    ToastUtils.showHint(response.message());
                    isCommitting=false;
                }
            }

            @Override
            public void onFail(Exception e) {
                isCommitting = false;
               ToastUtils.showError(e.toString());
            }
        });
    }
}
