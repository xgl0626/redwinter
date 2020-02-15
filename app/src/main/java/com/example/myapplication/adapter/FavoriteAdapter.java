package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.config.apiconfig;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;
import com.example.myapplication.holder.QuestionViewHolder;
import com.example.myapplication.holder.TailViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.holder.TailViewHolder.TYPE_QUESTION;

public class FavoriteAdapter extends RecyclerView.Adapter {
    private List<Question> mFavorite;
    private User mUser;
    Question mQuestion;
    private final static int TYPE_FAVORITE =0;
    private final static int TYPE_TAIL=1;
    public FavoriteAdapter(User user,Question question)
    {
        mQuestion=question;
        mUser=user;
        mFavorite=new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_TAIL : TYPE_QUESTION;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case TYPE_FAVORITE:
                QuestionViewHolder questionViewHolder=new QuestionViewHolder(inflater.inflate(R.layout.msg_item,parent,false),mUser, (ArrayList<Question>) mFavorite);
                questionViewHolder.addOnClickListener();
                return  questionViewHolder;
            case TYPE_TAIL:
               final TailViewHolder tailViewHolder=new TailViewHolder(inflater.inflate(R.layout.item_,parent,false));
                tailViewHolder.getLoadTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String param = "page=" + mFavorite.size() / 20 + "&token=" + mUser.getToken();
                       tailViewHolder.load(apiconfig.FAVORITE_LIST, param, FavoriteAdapter.this, TailViewHolder.TYPE_FAVORITE);
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
                questionViewHolder.updata(mFavorite.get(position));
                break;
            case TYPE_TAIL:
                String param = "page=" + mFavorite.size() / 20 + "&token=" + mUser.getToken();
                ((TailViewHolder) holder).load(apiconfig.FAVORITE_LIST, param, FavoriteAdapter.this, TailViewHolder.TYPE_FAVORITE);
                break;
        }
    }
    public void refreshFavoriteList(ArrayList<Question> newQuestionList) {
        mFavorite.clear();
        addFavorite(newQuestionList);
    }

    public void addFavorite(ArrayList<Question> questionList) {
        mFavorite.addAll(questionList);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mFavorite.size()+1;
    }
}
