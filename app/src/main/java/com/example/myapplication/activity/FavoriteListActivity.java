package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.myapplication.adapter.FavoriteAdapter;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.Json;
import com.example.myapplication.httputil.ToastUtils;

public class FavoriteListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private FavoriteAdapter favoriteAdapter;
    private User mUser;
    private Question mQuestion;
    private boolean mLoad;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritelist);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        setUpActionBar();
        setSupportActionBar(toolbar);
        mUser = getIntent().getParcelableExtra("user");
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        recyclerView=(RecyclerView)findViewById(R.id.favoriteRv);
        setUpRecyclerView();
        setRefreshLayout();
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
    }

    @SuppressLint("ResourceAsColor")
    private void setRefreshLayout() {
        mLoad=false;
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite, menu);
        return true;
    }

    private void upData() {
        if(mLoad)
            return;
        HttpUtil.sendHttpRequest(apiconfig.FAVORITE_LIST, "token=" + mUser.getToken() + "&page=0", new HttpUtil.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(HttpUtil.Response response) {
                if(response.isSuccess())
                {
                    mLoad=true;
                    favoriteAdapter.refreshFavoriteList(Json.getQuestionList(response.bodyString()));
                }else
                {
                    ToastUtils.showError(response.message());
                }
            }

            @Override
            public void onFail(Exception e) {
                ToastUtils.showError(e.toString());
                swipeRefreshLayout.setRefreshing(false);
                mLoad = false;
            }
        });
    }

    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(FavoriteListActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        favoriteAdapter =new FavoriteAdapter(mUser,mQuestion);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(favoriteAdapter);

    }

   /* public void ActivityStart(Class<? extends Activity> cls){
       Intent intent = new Intent(FavoriteListActivity.this,cls);
        intent.putExtra("user", (Parcelable) mUser);
        startActivity(intent);
    }*/

}
