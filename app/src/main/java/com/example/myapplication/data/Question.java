package com.example.myapplication.data;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

public class Question implements Parcelable {

    /**
     * id : 2
     * title : 孤独的等待
     * content : 怎么还没有人来玩啊
     * images : http://ok4qp4ux0.bkt.clouddn.com/1485064307258
     * date : 2017-12-26 16:53:04
     * exciting : 0
     * naive : 0
     * recent : null
     * answerCount : 0
     * authorId : 2
     * authorName : Jay
     * authorAvatar : http://ok4qp4ux0.bkt.clouddn.com/img-222c4cafc0af1718a6a3b45224cf5229.jpg
     * is_exciting : false
     * is_naive : false
     * is_favorite : false，
     */

    private int id;
    private String title;
    private String content;
    private ArrayList<String> images= new ArrayList<>();
    private String date;
    private int excitingCount;
    private int naiveCount;
    private Object recent;
    private int answerCount;
    private int authorId;
    private String authorName;
    private String authorAvatar;
    private boolean is_exciting;
    private boolean is_naive;
    public Question(){}

    private Question(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        date = in.readString();
        recent = in.readString();
        answerCount = in.readInt();
        authorId = in.readInt();
        excitingCount = in.readInt();
        naiveCount = in.readInt();
        authorName = in.readString();
        authorAvatar = in.readString();
        images = in.createStringArrayList();
        is_naive = in.readByte() != 0;
        is_exciting = in.readByte() != 0;
        is_favorite = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getImageUrl(int index) {
        return images.get(index);
    }

    public void addImageUrl(String url) {
        images.add(url);
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getExcitingCount() {
        return excitingCount;
    }

    public void setExcitingCount(int excitingCount) {
        this.excitingCount = excitingCount;
    }

    public int getNaiveCount() {
        return naiveCount;
    }

    public void setNaiveCount(int naiveCount) {
        this.naiveCount = naiveCount;
    }

    public Object getRecent() {
        return recent;
    }

    public void setRecent(Object recent) {
        this.recent = recent;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public boolean isIs_exciting() {
        return is_exciting;
    }

    public void setIs_exciting(boolean is_exciting) {
        this.is_exciting = is_exciting;
    }

    public boolean isIs_naive() {
        return is_naive;
    }

    public void setIs_naive(boolean is_naive) {
        this.is_naive = is_naive;
    }
    private boolean is_favorite;

    @Override
    public int describeContents() {
        return 0;
    }
    public boolean isFavorite() {
        return is_favorite;
    }

    public void setFavorite(boolean favorite) {
         is_favorite = favorite;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString((String) recent);
        dest.writeInt(answerCount);
        dest.writeInt(authorId);
        dest.writeInt(excitingCount);
        dest.writeInt(naiveCount);
        dest.writeString(authorName);
        dest.writeString(authorAvatar);
        dest.writeStringList(Collections.<String>singletonList(String.valueOf(images)));
        dest.writeByte((byte) (is_naive ? 1 : 0));
        dest.writeByte((byte) (is_exciting ? 1 : 0));
        dest.writeByte((byte) (is_favorite ? 1 : 0));
    }
}
