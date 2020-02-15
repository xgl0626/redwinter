package com.example.myapplication.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.AnswerAdapter;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Answer;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;
import com.example.myapplication.httputil.BitmapUtil;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.MyApplication;
import com.example.myapplication.view.CircleImageView;

import java.util.ArrayList;

public class AnswerVIewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    CircleImageView answerAvater;
    private ImageView answerImg;
    private TextView answerName;
    private TextView answerTime;
    private TextView answerContent;
    private TextView a_exciteCounts;
    private TextView a_nativeCounts;
    private ImageButton a_excite;
    private ImageButton a_native;
    private ImageButton a_like;
    private User mUser;
    private Question mQuestion;
    private ArrayList<Answer> mAnswerList ;
    public AnswerVIewHolder(@NonNull View itemView,User user, ArrayList answerArrayList) {
        super(itemView);
        answerAvater=(CircleImageView)itemView.findViewById(R.id.avatar);
        a_excite=(ImageButton)itemView.findViewById(R.id.excitingButton);
        a_native=(ImageButton)itemView.findViewById(R.id.naiveButton);
        a_like=(ImageButton)itemView.findViewById(R.id.acceptButton);
        a_exciteCounts=(TextView)itemView.findViewById(R.id.excitingCount);
        a_nativeCounts=(TextView)itemView.findViewById(R.id.naiveCount);
        answerName=(TextView)itemView.findViewById(R.id.authorName);
        answerTime=(TextView)itemView.findViewById(R.id.date);
        answerContent=(TextView)itemView.findViewById(R.id.answerContent);
        mUser=user;
        mAnswerList=answerArrayList;
    }
    public void setAcceptButtonVisible(boolean visible) {
        a_like.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    public void updata(Answer answer,Question question)
    {
        answerName.setText(answer.getAuthorName());
        answerTime.setText(answer.getDate());
        answerContent.setText(answer.getContent());
        a_nativeCounts.setText("("+answer.getNaiveCount()+")");
        a_exciteCounts.setText("("+answer.getExcitingCount()+")");
        if (answer.getAuthorAvatar().equals("null"))
            answerAvater.setImageResource(R.drawable.first);
        else
            HttpUtil.loadImage(answer.getAuthorAvatar(), new HttpUtil.Callback() {
                @Override
                public void onResponse(HttpUtil.Response response) {
                    if (response.isSuccess())
                        answerAvater.setImageBitmap(BitmapUtil.toBitmap(response.bodyBytes()));
                }

                @Override
                public void onFail(Exception e) {

                }
            });
        a_like.setBackgroundResource(answer.isIs_Best()? R.drawable.favorite:R.drawable.close);
        a_excite.setBackgroundResource(answer.isIs_exciting()? R.drawable.like_filled:R.drawable.like);
        a_native.setBackgroundResource(answer.isIs_naive()? R.drawable.star_filled:R.drawable.star);
    }

    @Override
    public void onClick(View v) {
        Answer answer=mAnswerList.get(getLayoutPosition());
        String param="id"+answer.getId()+"&type=2&yoken="+mUser.getToken();
      switch (v.getId())
      {
          case R.id.naiveButton:
              if (answer.isIs_naive()) {
                  HttpUtil.sendHttpRequest(apiconfig.CANCEL_NAIVE, param);
                  a_native.setBackgroundResource(R.drawable.star_filled);
                  answer.setNaiveCount(answer.getNaiveCount() - 1);
                  answer.setIs_naive(false);
              } else {
                  HttpUtil.sendHttpRequest(apiconfig.NAIVE, param);
                  a_native.setBackgroundResource(R.drawable.star);
                  answer.setNaiveCount(answer.getNaiveCount() + 1);
                  answer.setIs_naive(true);
              }
              a_nativeCounts.setText("(" + answer.getNaiveCount() + ")");
              break;
          case R.id.excitingButton:
              if(answer.isIs_Best()) {
                  HttpUtil.sendHttpRequest(apiconfig.CANCEL_EXCITING, param);
                  a_excite.setBackgroundResource(R.drawable.like_filled);
                  answer.setExcitingCount(answer.getExcitingCount()-1);
                  answer.setIs_Best(false);
              }
              else
              {
                  HttpUtil.sendHttpRequest(apiconfig.EXCITING, param);
                  a_excite.setBackgroundResource(R.drawable.like);
                  answer.setExcitingCount(answer.getExcitingCount()+1);
                  answer.setIs_Best(true);
              }
              a_exciteCounts.setText("(" + answer.getExcitingCount() + ")");
              break;
          case R.id.acceptButton:
              param = "qid=" + mQuestion.getId() + "&aid=" + answer.getId() + "&token=" + mUser.getToken();
              HttpUtil.sendHttpRequest(apiconfig.ACCEPT,param);
              a_like.setBackgroundResource(R.drawable.favorite);
              answer.setIs_Best(true);
              break;
      }
    }

    public void addOnClickListener() {
        a_like.setOnClickListener(this);
        a_native.setOnClickListener(this);
        a_excite.setOnClickListener(this);
    }
}
