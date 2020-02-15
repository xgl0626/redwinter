package com.example.myapplication.holder;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AnswerAdapter;
import com.example.myapplication.adapter.FavoriteAdapter;
import com.example.myapplication.adapter.QuestionAdapter;
import com.example.myapplication.data.Question;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.Json;
import com.example.myapplication.httputil.ToastUtils;

import java.lang.reflect.Type;

public class TailViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_QUESTION = 0;
    public static final int TYPE_ANSWER = 1;
    public static final int TYPE_FAVORITE = 2;
    private TextView textView;
    private boolean loading;

    @SuppressLint("ResourceType")
    public TailViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.load);
        loading = false;
    }

    public View getLoadTextView() {
        return textView;
    }

    public void load(String address, String param, final RecyclerView.Adapter adapter, final int type) {
        int n = 1;  //Answer列表特殊处理
        if (type == TYPE_ANSWER)
            n = 2;
        if ((adapter.getItemCount() - n) % 10 != 0) {
            textView.setText("没有更多了");
            return;
        }
        if (loading)
            return;
        textView.setText("加载中");
        loading = true;
        switch (type){
            case TYPE_QUESTION:
                loading = false;
            HttpUtil.sendHttpRequest(address, param, new HttpUtil.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(HttpUtil.Response response) {
                    if (response.isSuccess()) {
                        String key = "questions";
                        String data = Json.getElement(response.bodyString(), key);
                        if (data == null || data.equals("null") || data.equals("[]")) {
                            textView.setText("没有更多了");
                            return;
                        }
                        ((QuestionAdapter) adapter).addQuestion(Json.getQuestionList(response.bodyString()));
                    } else {
                        textView.setText("加载失败");
                    }
                }

                @Override
                public void onFail(Exception e) {
                }
            });
            break;
            case TYPE_ANSWER:
                loading = false;
            HttpUtil.sendHttpRequest(address, param, new HttpUtil.Callback() {
                @Override
                public void onResponse(HttpUtil.Response response) {
                    if (response.isSuccess()) {
                        String key = "answers";
                        String data = Json.getElement(response.bodyString(), key);
                        if (data == null || data.equals("null") || data.equals("[]")) {
                            textView.setText("没有更多了");
                            return;
                        }
                        ((AnswerAdapter) adapter).addAnswer(Json.getAnswerList(response.bodyString()));
                    } else {
                        textView.setText("加载失败");
                    }
                }

                @Override
                public void onFail(Exception e) {
                }
            });
            break;
            case TYPE_FAVORITE:
                loading = false;
            HttpUtil.sendHttpRequest(address, param, new HttpUtil.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(HttpUtil.Response response) {
                    if (response.isSuccess()) {
                        String key = "questions";
                        String data = Json.getElement(response.bodyString(), key);
                        if (data == null || data.equals("null") || data.equals("[]")) {
                            textView.setText("没有更多了");
                            return;
                        }

                        ((FavoriteAdapter) adapter).addFavorite(Json.getQuestionList(response.bodyString()));
                    }

                }

                @Override
                public void onFail(Exception e) {
                }
            });
            break;
            default:return;
        }
        }
    }
