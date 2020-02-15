package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.User;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.R.id.main_gridView_item_photo;

public class QuestionActivity extends photoActivity {
    private User mUser;
    private Toolbar toolbar;
    private EditText title;
    private EditText content;
    private String mTitle;
    private Button back_top;
    private boolean isCommitting;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qusetion_activity);
        title=(EditText)findViewById(R.id.questionTitle);
        content=(EditText)findViewById(R.id.questionDetail);
        mTitle=title.getText().toString();
        back_top=(Button)findViewById(R.id.back_up);
        back_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUser = getIntent().getParcelableExtra("user");
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        setUpActionBar();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_commit, menu);
        return true;
    }
    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void upCommit() {
        if (isCommitting)
            return;
        String savetitle=title.getText().toString();
        String savecontent = content.getText().toString();
        if (savecontent.equals("")) {
            ToastUtils.showHint("内容不能为空");
            return;
        }
        if (savetitle.equals("")) {
          ToastUtils.showHint("标题不能为空");
            return;
        }

        isCommitting=true;
        HttpUtil.sendHttpRequest(apiconfig.POST_QUESTION, "title=" + savetitle+ "&content=" + savecontent + "&token=" + mUser.getToken(), new HttpUtil.Callback() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                if(response.isSuccess())
                {
                    finish();
                }
                else
                {
                    isCommitting=false;
                    ToastUtils.showError(response.message());
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
