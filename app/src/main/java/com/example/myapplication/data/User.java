package com.example.myapplication.data;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    /**
     * id : 2
     * username : Jay
     * password : 123456
     * avatar : null
     * token : ac2f704deb121877d0895cf5bb96716981610c5f
     */

    private int id;
    private String username;
    private String password;
    private String avatar;
    private String token;
    public User() {}

    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        avatar = in.readString();
        token = in.readString();
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(avatar);
        dest.writeString(token);
    }

}
