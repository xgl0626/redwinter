package com.example.myapplication.httputil;

import android.widget.Toast;

public class ToastUtils {
    public static void showError(String error) {
        Toast.makeText(MyApplication.getContext(), error, Toast.LENGTH_LONG).show();
    }

    public static void showHint(String result) {
        Toast.makeText(MyApplication.getContext(), result, Toast.LENGTH_SHORT).show();
    }
}
