package com.example.myapplication.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;

public class Answer {
    /**
     * id : 1
     * content : 自娱自乐
     * images : http://ok4qp4ux0.bkt.clouddn.com/img-222c4cafc0af1718a6a3b45224cf5229.jpg
     * date : 2017-12-26 16:56:33
     * best : 0
     * exciting : 0
     * naive : 0
     * authorId : 2
     * authorName : Jay
     * authorAvatar : http://ok4qp4ux0.bkt.clouddn.com/img-222c4cafc0af1718a6a3b45224cf5229.jpg
     * is_exciting : false
     * is_naive : false
     */

    private int id;
    private String content;
    private ArrayList<String> imageUrlStrings = new ArrayList<>();
    private String date;
    private boolean best;
    private int excitingCount;
    private int naiveCount;
    private int authorId;
    private String authorName;
    private String authorAvatar;
    private boolean is_exciting;
    private boolean is_naive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages(int index) {
        return imageUrlStrings.get(index);
    }

    public void setImages(String url) {
        imageUrlStrings.add(url);
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

    public boolean isIs_Best() {
        return best;
    }

    public void setIs_Best(boolean best) {
        this.best = best;
    }
}
