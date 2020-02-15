package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.User;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.Json;
import com.example.myapplication.httputil.ToastUtils;
import com.google.gson.JsonParser;

public class changePasswordActivity extends AppCompatActivity {
    private Button newPassword;
    private EditText new_pass;
    private EditText old_pass;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private User user;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);
        user = getIntent().getParcelableExtra("user");
        newPassword=(Button)findViewById(R.id.n_pass);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.back_quest:
                        finish();
                        break;
                    default:
                }
                return true;
            }
        });
        new_pass=(EditText) findViewById(R.id.new_psw);
        old_pass=(EditText)findViewById(R.id.old_password);
        sharedPreferences=getSharedPreferences("account",MODE_PRIVATE);
        newPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password=new_pass.getText().toString();
                final String oldpassword=old_pass.getText().toString();
                if(password.equals("")||oldpassword.equals(""))
                {
                    Toast.makeText(changePasswordActivity.this,"存在密码为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 5) {
                    Toast.makeText(changePasswordActivity.this,"密码过短",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals(oldpassword))
                {
                    Toast.makeText(changePasswordActivity.this,"与旧密码重复",Toast.LENGTH_SHORT).show();
                    return;
                }
                String param="password=" +password+"&token=" + user.getToken();
                HttpUtil.sendHttpRequest(apiconfig.CHANGE_PASSWORD, param, new HttpUtil.Callback() {
                    @Override
                    public void onResponse(HttpUtil.Response response) {
                        if(response.isSuccess()) {
                           ToastUtils.showHint(response.getInfo());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("password", password);
                            editor.putBoolean("ischecked",false);
                            editor.apply();
                            user.setToken(Json.getElement(response.bodyString(),"token"));
                            finish();
                        }
                        else
                           ToastUtils.showError(response.message());
                    }

                    @Override
                    public void onFail(Exception e) {
                        ToastUtils.showError(e.toString());
                    }
                });
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.favorite,menu);
        return true;
    }
    }
