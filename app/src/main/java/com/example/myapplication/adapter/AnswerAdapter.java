package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.QuestionlistActivity;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Answer;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;
import com.example.myapplication.holder.AnswerVIewHolder;
import com.example.myapplication.holder.QuestionViewHolder;
import com.example.myapplication.holder.TailViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.myapplication.R.layout.msg_answer;

public class AnswerAdapter extends RecyclerView.Adapter {
    private static final int TYPE_QUESTION = 0;
    private static final int TYPE_TAIL = 2;
    private User mUser;
    private Question mQuestion;
    private List<Answer> mAnswerList;
    public final static int TYPE_ANSWER=1;

    public AnswerAdapter(User user, Question question)
    {
        mQuestion = question;
        mUser=user;
        mAnswerList=new ArrayList<>();
        sort();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_QUESTION;
        else if (position == getItemCount() - 1)
            return TYPE_TAIL;
        else return TYPE_ANSWER;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_QUESTION:
                ArrayList<Question> mQuestionList = new ArrayList<>();
                mQuestionList.add(mQuestion);
                QuestionViewHolder questionViewHolder = new QuestionViewHolder(inflater.inflate(R.layout.msg_item,parent,false),mUser, (ArrayList<Question>) mQuestionList);
                questionViewHolder.addOnClickListener();
                return questionViewHolder;
            case TYPE_ANSWER:
                AnswerVIewHolder answerViewHolder = new AnswerVIewHolder(inflater.inflate(R.layout.msg_answer,parent,false), mUser, (ArrayList) mAnswerList);
                answerViewHolder.addOnClickListener();
                if (mQuestion.getAuthorId() == mUser.getId())
                    answerViewHolder.setAcceptButtonVisible(true);
                return answerViewHolder;
            case TYPE_TAIL:
                final TailViewHolder tailViewHolder = new TailViewHolder(inflater.inflate(R.layout.item_,parent,false));
                tailViewHolder.getLoadTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String param = "page=" + mAnswerList.size() / 20 + "&qid=" + mQuestion.getId() + "&token=" + mUser.getToken();
                        tailViewHolder.load(apiconfig.ANSWER_LIST, param, AnswerAdapter.this, TailViewHolder.TYPE_ANSWER);
                    }
                });
                return tailViewHolder;
            default:
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position))
        {
            case TYPE_QUESTION :
                QuestionViewHolder questionViewHolder=(QuestionViewHolder)holder;
                questionViewHolder.updata(mQuestion);
                break;
            case TYPE_ANSWER:
                AnswerVIewHolder answerVIewHolder=(AnswerVIewHolder)holder;
                answerVIewHolder.updata(mAnswerList.get(position-1),mQuestion);
                break;
            case TYPE_TAIL:
                String param = "page=" + mAnswerList.size() / 20 + "&qid=" + mQuestion.getId() + "&token=" + mUser.getToken();
                ((TailViewHolder) holder).load(apiconfig.ANSWER_LIST,param,AnswerAdapter.this,TYPE_ANSWER);
        }
    }

    @Override
    public int getItemCount() {
        return mAnswerList.size()+2;
    }
    public void refreshAnswerList(ArrayList<Answer> newAnswerList) {
        mAnswerList.clear();
        addAnswer(newAnswerList);
    }
    public void addAnswer(ArrayList<Answer> AnswerList) {
        mAnswerList.addAll(AnswerList);
        sort();
        notifyDataSetChanged();
    }
    private void sort() {
        Collections.sort(mAnswerList, new Comparator<Answer>() {
            @Override
            public int compare(Answer o1, Answer o2) {
                if (o1.isIs_Best())
                    return -1;
                else if (o2.isIs_Best())
                    return 1;
                else return o2.getDate().compareTo(o1.getDate());
            }
        });
    }
}
