package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SpinnerAdapter;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AnswerAdapter;
import com.example.myapplication.adapter.FavoriteAdapter;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.Json;
import com.example.myapplication.httputil.ToastUtils;

public class AnswerListActivity<actionBar> extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    private User mUser;
    private Question mQuestion;
    private AnswerAdapter answerAdapter;
    private boolean mLoad;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_answerlist);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        setUpActionBar();
        setSupportActionBar(toolbar);
        Bundle data = getIntent().getBundleExtra("data");
        mUser = data.getParcelable("user");
        mQuestion = data.getParcelable("question");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.answerRv);
        setUpRecyclerView();
        setUpRefreshLayout();
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.back_quest:
                        finish();
                        break;
                    default:
                }
                return true;
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.favorite,menu);
        return true;
    }
    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void setUpRefreshLayout() {
        mLoad=false;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void upData() {
        if(mLoad)
            return;
        HttpUtil.sendHttpRequest(apiconfig.ANSWER_LIST, "qid=" + mQuestion.getId() + "&page=0&token=" + mUser.getToken(), new HttpUtil.Callback() {
            @Override
            public void onResponse(HttpUtil.Response response) {
                if(response.isSuccess())
                {
                    mLoad=true;
                   answerAdapter.refreshAnswerList(Json.getAnswerList(response.bodyString()));
                }else
                {
                    ToastUtils.showHint(response.message());
                }
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(AnswerListActivity.this,e.toString(),Toast.LENGTH_SHORT).show();;
               swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showError(e.toString());
                mLoad = false;
            }
        });
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(AnswerListActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        answerAdapter = new AnswerAdapter(mUser,mQuestion);
        recyclerView.setAdapter(answerAdapter);

    }

}
