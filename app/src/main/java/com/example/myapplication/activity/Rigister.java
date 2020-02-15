package com.example.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Question;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.ToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.myapplication.config.apiconfig.REGISTER;

public class Rigister extends AppCompatActivity {
    private EditText username;
    private EditText password1;
    private EditText password2;
    private SharedPreferences sharedPreferences;
    private Button rigister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rigister);
        username = (EditText) findViewById(R.id.rg_username);
        password1 = (EditText) findViewById(R.id.rg_psw);
        password2 = (EditText) findViewById(R.id.rg_psw2);
        rigister = (Button) findViewById(R.id.rigister);
        sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        rigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paw1 = password1.getText().toString();
                String paw2 = password2.getText().toString();
                String user = username.getText().toString();
                initrigister(paw1, paw2, user);
            }
        });
    }

    private void initrigister(String paw1, String paw2, String user) {
        if (user.equals("") || paw1.equals("") || paw2.equals("")) {
            Toast.makeText(Rigister.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
         if (!paw1.equals(paw2)) {
            Toast.makeText(Rigister.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return;
         }
           Register(apiconfig.REGISTER, user, paw1);
    }

    private void Register(String address, String user, String paw1) {
        final ProgressDialog progressDialog = new ProgressDialog(Rigister.this);
        progressDialog.setMessage("登录中...");
        progressDialog.show();
        HttpUtil.sendHttpRequest(address, "username=" + user + "&password=" + paw1, new HttpUtil.Callback() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                checkResponseStatusCode(response.getStatusCode(), response);
                progressDialog.dismiss();
            }
            @Override
            public void onFail(Exception e) {
                ToastUtils.showError(e.toString());
                e.printStackTrace();
                progressDialog.dismiss();
            }

        });
    }
    private void checkResponseStatusCode ( int statusCode, final HttpUtil.Response response){
        switch (statusCode) {
            case 200:
                ToastUtils.showHint("欢迎来到逼乎");
                String paw1 = password1.getText().toString();
                String user = username.getText().toString();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("username",user);
                editor.putString("password",paw1);
                editor.apply();
                actionStart(response.bodyString());
                break;
            case 400:
                ToastUtils.showHint(response.message());
                Log.d("tag",response.getInfo());
                break;
            default: ToastUtils.showError(response.message());
                break;
        }
    }
    private void actionStart(String data) {
        Intent intent = new Intent(Rigister.this, QuestionlistActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
        finish();
    }
}
