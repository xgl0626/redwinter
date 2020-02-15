package com.example.myapplication.holder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.AnswerActivity;
import com.example.myapplication.activity.AnswerListActivity;
import com.example.myapplication.activity.QuestionlistActivity;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;
import com.example.myapplication.httputil.BitmapUtil;
import com.example.myapplication.httputil.DataUtil;
import com.example.myapplication.httputil.HttpUtil;
import com.example.myapplication.httputil.MyApplication;
import com.example.myapplication.httputil.ToastUtils;
import com.example.myapplication.view.CircleImageView;

import java.util.ArrayList;

public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CircleImageView authorAvatar;
    private TextView name;
    private TextView content;
    private TextView title;
    private TextView show_data;
    private TextView recent_data;
    private TextView nativeCounts;
    private TextView answerCounts;
    private TextView exciteCounts;
    private ImageView questionImg;
    private ImageButton mExcitingButton;
    private ImageButton mNaiveButton;
    private ImageButton mAnswerButton;
    private ImageButton mFavoriteButton;

    private User mUser;
    private ArrayList<Question> mQuestionList;

    public QuestionViewHolder(@NonNull View itemView, User user, ArrayList<Question> questionList) {
        super(itemView);
        setItemViewOnClickListener(itemView);
        authorAvatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.authorName);
        content = (TextView) itemView.findViewById(R.id.questionDetail);
        show_data = (TextView) itemView.findViewById(R.id.date);
        recent_data = (TextView) itemView.findViewById(R.id.recentDate);
        title = (TextView) itemView.findViewById(R.id.questionTitle);
        nativeCounts = (TextView) itemView.findViewById(R.id.naiveCount);
        exciteCounts = (TextView) itemView.findViewById(R.id.excitingCount);
        answerCounts = (TextView) itemView.findViewById(R.id.answerCount);
        mNaiveButton = (ImageButton) itemView.findViewById(R.id.naiveButton);
        mExcitingButton = (ImageButton) itemView.findViewById(R.id.excitingButton);
        mAnswerButton = (ImageButton) itemView.findViewById(R.id.answerButton);
        mFavoriteButton = (ImageButton) itemView.findViewById(R.id.favoriteButton);
        mUser = user;
        mQuestionList = questionList;
        questionImg=(ImageView)itemView.findViewById(R.id.questionImage);
    }
    private void setItemViewOnClickListener(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AnswerListActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("user", (Parcelable) mUser);
                data.putParcelable("question", mQuestionList.get(getLayoutPosition()));
                intent.putExtra("data", data);
                v.getContext().startActivity(intent);
            }
        });
    }
    public void updata(Question question)
    {
        name.setText(question.getAuthorName());
        content.setText(question.getContent());
        show_data.setText(question.getDate());
        recent_data.setText(DataUtil.getDateDescription((String) question.getRecent()) + " 更新");
        title.setText(question.getTitle());
        exciteCounts.setText("(" + question.getExcitingCount() + ")");
        nativeCounts.setText("(" + question.getNaiveCount() + ")");
        answerCounts.setText("(" + question.getAnswerCount() + ")");
        mNaiveButton.setBackgroundResource(question.isIs_naive() ? R.drawable.star : R.drawable.star_filled);
        mExcitingButton.setBackgroundResource(question.isIs_exciting() ? R.drawable.like : R.drawable.like_filled);
        mFavoriteButton.setBackgroundResource(question.isFavorite() ? R.drawable.close : R.drawable.favorite);
        if(question.getAuthorAvatar().equals("null"))
        {
            authorAvatar.setImageResource(R.drawable.first);
        }
        else
        {
            HttpUtil.loadImage(question.getAuthorAvatar(), new HttpUtil.Callback() {
                @Override
                public void onResponse(HttpUtil.Response response) {
                    if (response.isSuccess())
                        authorAvatar.setImageBitmap(BitmapUtil.toBitmap(response.bodyBytes()));
                }

                @Override
                public void onFail(Exception e) {

                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        Question question = mQuestionList.get(getLayoutPosition());
        String param = "id=" + question.getId() + "&type=1&token=" + mUser.getToken();
        switch (v.getId()) {
            case R.id.naiveButton:
                if (question.isIs_naive()) {
                    HttpUtil.sendHttpRequest(apiconfig.CANCEL_NAIVE, param);
                    mNaiveButton.setBackgroundResource(R.drawable.star_filled);
                    question.setNaiveCount(question.getNaiveCount() - 1);
                    question.setIs_naive(false);
                } else {
                    HttpUtil.sendHttpRequest(apiconfig.NAIVE, param);
                    mNaiveButton.setBackgroundResource(R.drawable.star);
                    question.setNaiveCount(question.getNaiveCount() + 1);
                    question.setIs_naive(true);
                }
                nativeCounts.setText("(" + question.getNaiveCount() + ")");
                break;
            case R.id.excitingButton:
                if (question.isIs_exciting()) {
                    HttpUtil.sendHttpRequest(apiconfig.CANCEL_EXCITING, param);
                    mExcitingButton.setBackgroundResource(R.drawable.like);
                    question.setExcitingCount(question.getExcitingCount() - 1);
                    question.setIs_exciting(false);
                } else {
                    HttpUtil.sendHttpRequest(apiconfig.EXCITING, param);
                    mExcitingButton.setBackgroundResource(R.drawable.like_filled);
                    question.setExcitingCount(question.getExcitingCount() + 1);
                    question.setIs_exciting(true);
                }
                exciteCounts.setText("(" + question.getExcitingCount() + ")");
                break;
            case R.id.favoriteButton:
                param = "qid=" + question.getId() + "&token=" + mUser.getToken();
                if (question.isFavorite()) {
                    HttpUtil.sendHttpRequest(apiconfig.CANCEL_FAVORITE, param);
                    mFavoriteButton.setBackgroundResource(R.drawable.favorite);
                    question.setFavorite(false);
                } else {
                    HttpUtil.sendHttpRequest(apiconfig.FAVORITE, param);
                    mFavoriteButton.setBackgroundResource(R.drawable.close);
                    question.setFavorite(true); }
                break;
        }
    }
    public void addOnClickListener()
    {
        mExcitingButton.setOnClickListener(this);
        mFavoriteButton.setOnClickListener(this);
        mNaiveButton.setOnClickListener(this);
        mAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_answer = new Intent(v.getContext(), AnswerActivity.class);
                intent_answer.putExtra("qid", mQuestionList.get(getLayoutPosition()).getId());
                intent_answer.putExtra("token", mUser.getToken());
                v.getContext().startActivity(intent_answer);
            }
        });
    }
}
