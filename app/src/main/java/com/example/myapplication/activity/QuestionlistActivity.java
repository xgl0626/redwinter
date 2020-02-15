package com.example.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.QuestionAdapter;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;
import com.example.myapplication.httputil.BitmapUtil;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.Json;
import com.example.myapplication.httputil.MyApplication;
import com.example.myapplication.httputil.ToastUtils;
import com.example.myapplication.view.CircleImageView;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class QuestionlistActivity extends photoActivity {
    private Toolbar toolbar;
    private User mUser;
    private Question question;
    private CircleImageView mAvatar;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private QuestionAdapter questionAdapter;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean mLoading;
    protected static final int OPEN_ALBUM = 0;
    protected static final int CROP_IMAGE = 1;
    private  Uri imageUri;
    private static final int UPDATE_TEXT=1;
  private Handler handler= new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what)
          {
              case UPDATE_TEXT:
                  Open();
          }

      }
  };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_qusertionlist);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mUser = Json.getUser(getIntent().getStringExtra("data"));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_user);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
               {
                    case R.id.it_question:
                        ActivityStart(QuestionActivity.class);
                        break;
                    default:
                }
                return true;
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(QuestionlistActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        questionAdapter =new QuestionAdapter(mUser);
        recyclerView.setAdapter(questionAdapter);
        setUpRefreshLayout();
        setUpNavigationView();
    }
    private void setUpRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                uploadData();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void uploadData() {
        if (mLoading)
            return;
        mLoading = true;
        HttpUtil.sendHttpRequest(apiconfig.QUESTION_LIST, "page=0" + "&token=" + mUser.getToken(), new HttpUtil.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(HttpUtil.Response response) {
                if(response.isSuccess())
                {
                    questionAdapter.refreshQuestionList(Json.getQuestionList(response.bodyString()));
                }
                else
                    Toast.makeText(QuestionlistActivity.this,response.message(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(QuestionlistActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                mRefreshLayout.setRefreshing(false);
                mLoading = false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    private void setUpNavigationView() {
        //menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.favorite:ActivityStart(FavoriteListActivity.class);
                    break;
                case R.id.persondata:ActivityStart(PersondataActivity.class);
                    break;
                case R.id.changePassword: ActivityStart(changePasswordActivity.class);
                    break;
                case R.id.out: out();
                    break;
                default:
            }
            return true;
        }
    });
        //personheader
        View view =navigationView.inflateHeaderView(R.layout.nav_header);
        TextView username_header = (TextView) view.findViewById(R.id.name);
        username_header.setText(mUser.getUsername());
        mAvatar = (CircleImageView) view.findViewById(R.id.icon_img);
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Message message=new Message();
                message.what=UPDATE_TEXT;
                handler.sendMessage(message);
            }
        });
        if (!mUser.getAvatar().equals("null"))
            HttpUtil.loadImage((String)mUser.getAvatar(), new HttpUtil.Callback() {
                @Override
                public void onResponse(HttpUtil.Response response) {
                    if (response.isSuccess())
                        mAvatar.setImageBitmap(BitmapUtil.toBitmap(response.bodyBytes()));
                    else {
                        ToastUtils.showError(response.message());
                    }
                }

                @Override
                public void onFail(Exception e) {
                    e.printStackTrace();
                    ToastUtils.showError(e.toString());
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuitem) {
        navigationView.setCheckedItem(R.id.persondata);
        switch (menuitem.getItemId())
        { case android.R.id.home:
            mDrawerLayout.openDrawer(GravityCompat.START);
            break;
            default:
            mDrawerLayout.closeDrawers();
            return true;
        }
        return super.onOptionsItemSelected(menuitem);
    }

    private void  upLoadAvatar(Uri uri) {
            Bitmap avatar = BitmapUtil.toBitmap(uri);
            mAvatar.setImageBitmap(avatar);
            String name = System.currentTimeMillis() + "";
            String param = "token=" + mUser.getToken() + "&avatar=" + apiconfig.QINIU_URL + name;
            HttpUtil.uploadImage(BitmapUtil.toBytes(avatar), name, param, apiconfig.MODIFY_AVATAR);
        }

        public void Open() {
        CharSequence[] items = {"拍照", "图库"};// 裁剪items选项
        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
       new AlertDialog.Builder(QuestionlistActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                break;
                            case 1:
                                checkAndOpenAlbum();
                              break;
                        }

                    }
                }).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OPEN_ALBUM:
                    /*cropImage(BitmapUtil.parseImageUriString(data),"file://" + getExternalCacheDir() + "/" + System.currentTimeMillis());
                    break;
                case CROP_IMAGE:
                    Log.d("tag",""+data.getData());*/
                    upLoadAvatar(data.getData());
                    break;
            }
        }
    }
    public void out()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void ActivityStart(Class<? extends Activity> cls){
        Intent intent=new Intent(QuestionlistActivity.this,cls);
        intent.putExtra("user", (Parcelable) mUser);
        startActivity(intent);
    }
}
