package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;
import com.example.myapplication.holder.QuestionViewHolder;
import com.example.myapplication.holder.TailViewHolder;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter {
    private static final int TYPE_QUESTION = 0;
    private static final int TYPE_update = 1;
    private List<Question> mQuestionList;
    private User mUser;
    public QuestionAdapter(User user)
    {
        mUser=user;
        mQuestionList=new ArrayList<>();
    }
    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_update : TYPE_QUESTION;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        switch(viewType)
        {
            case TYPE_QUESTION:
            QuestionViewHolder questionViewHolder=new QuestionViewHolder(inflater.inflate(R.layout.msg_item,parent,false),mUser, (ArrayList<Question>) mQuestionList);
                questionViewHolder.addOnClickListener();
            return questionViewHolder;
            case TYPE_update: final TailViewHolder tailViewHolder = new TailViewHolder(inflater.inflate(R.layout.item_, parent, false));
                tailViewHolder.getLoadTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String param = "page=" + mQuestionList.size() / 20 + "&token=" + mUser.getToken();
                        tailViewHolder.load(apiconfig.QUESTION_LIST, param, QuestionAdapter.this, TailViewHolder.TYPE_QUESTION);
                    }
                });
                return tailViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_QUESTION:
                QuestionViewHolder questionViewHolder = (QuestionViewHolder) holder;
                questionViewHolder.updata(mQuestionList.get(position));
                break;
            case TYPE_update:
                String param = "page=" + mQuestionList.size() / 10 + "&token=" + mUser.getToken();
                ((TailViewHolder) holder).load(apiconfig.QUESTION_LIST, param, this, TailViewHolder.TYPE_QUESTION);
                break;
        }
    }
    public void refreshQuestionList(ArrayList<Question> newQuestionList) {
        mQuestionList.clear();
        addQuestion(newQuestionList);
    }

    public void addQuestion(ArrayList<Question> questionList) {
        mQuestionList.addAll(questionList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mQuestionList.size()+1;
    }
}
