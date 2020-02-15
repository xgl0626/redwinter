package com.example.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.MyApplication;
import com.example.myapplication.httputil.ToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.myapplication.config.apiconfig.LOGIN;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private CheckBox isfirstlogin;
    private Button lg_rg;
    private Button login;
    private EditText user;
    private EditText pasw;
    private String input_username;
    private String input_psw;
    private boolean remember;
    private SharedPreferences checked;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checked=getSharedPreferences("ischeck",MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        remember = checked.getBoolean("ischecked", false);
        lg_rg = (Button) findViewById(R.id.bt_rg);
        login = (Button) findViewById(R.id.bt_login);
        user = (EditText) findViewById(R.id.lg_username);
        pasw = (EditText) findViewById(R.id.lg_psw);
        isfirstlogin=(CheckBox)findViewById(R.id.is_firstlogin);
        lg_rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lg_rgintent = new Intent(LoginActivity.this, Rigister.class);
                startActivity(lg_rgintent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor editor=checked.edit();
            @Override
            public void onClick(View v) {
                if(isfirstlogin.isChecked())
                {
                    editor.putBoolean("ischecked",true);
                    editor.putString("user",user.getText().toString());
                    editor.putString("pass",pasw.getText().toString());
                    editor.commit();
                }
                else
                {
                    editor.clear();
                    editor.putBoolean("ischecked",false);
                    editor.commit();
                }
                    input_username = user.getText().toString();
                    input_psw = pasw.getText().toString();
                if (input_username.equals("") || input_psw.equals("")) {
                    ToastUtils.showHint("用户名或者密码不能为空");
                }
                login(LOGIN, input_username, input_psw);
            }
        });
        if (remember) {
            initlogin();
        }
    }

    private void initlogin() {
        String u=checked.getString("user","");
        String p=checked.getString("pass","");
        user.setText(u);
        pasw.setText(p);
        isfirstlogin.setChecked(true);
    }

    private void login(String address, String username, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登录中...");
        progressDialog.show();
        HttpUtil.sendHttpRequest(address, "username=" + username + "&password=" + password, new HttpUtil.Callback() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                checkResponseStatusCode(response.getStatusCode(), response);
                progressDialog.dismiss();
            }
            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
                ToastUtils.showError(e.toString());
                progressDialog.dismiss();
            }

        });
    }
    private void checkResponseStatusCode(int statusCode, final HttpUtil.Response response) {
        switch (statusCode) {
            case 200:
                ToastUtils.showHint("欢迎来到逼乎");
                actionStart(response.bodyString());
                break;
            case 400:
                ToastUtils.showHint(response.message());
                Log.d("tag",response.getInfo());
                break;
            default:
                ToastUtils.showError(response.message());
                break;
        }
    }
    private void actionStart(String data) {
        Intent intent = new Intent(LoginActivity.this, QuestionlistActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
    }
}
