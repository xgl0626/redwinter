package com.example.myapplication.httputil;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.myapplication.data.Answer;
import com.example.myapplication.data.Question;
import com.example.myapplication.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Json{
        public static User getUser(String data) {
            User user = new User();
            try {
                JSONObject object = new JSONObject(data);
                user.setId(object.getInt("id"));
                user.setAvatar(object.getString("avatar"));
                user.setToken(object.getString("token"));
                user.setUsername(object.getString("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return user;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public static ArrayList<Question> getQuestionList(String data) {
            ArrayList<Question> questionList = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data);
                JSONArray array = object.getJSONArray("questions");
                for (int i = 0; i < array.length(); i++) {
                    Question question = new Question();
                    JSONObject js = array.getJSONObject(i);
                    question.setId(js.getInt("id"));
                    question.setTitle(js.getString("title"));
                    question.setContent(js.getString("content"));
                    question.setDate(js.getString("date"));
                    question.setRecent(js.getString("recent"));
                    if (question.getRecent().equals("null"))
                        question.setRecent(question.getDate());
                    question.setAnswerCount(js.getInt("answerCount"));
                    question.setAuthorId(js.getInt("authorId"));
                    question.setAuthorName(js.getString("authorName"));
                    question.setAuthorAvatar(js.getString("authorAvatar"));
                    question.setExcitingCount(js.getInt("exciting"));
                    question.setNaiveCount(js.getInt("naive"));
                    question.setIs_exciting(js.getBoolean("is_exciting"));
                    question.setIs_naive(js.getBoolean("is_naive"));
                    if (js.has("is_favorite"))
                        question.setFavorite(js.getBoolean("is_favorite"));
                    else question.setFavorite(true);
                    if(js.has("images")) {
                        String images = js.getString("images");
                            question.addImageUrl(images);
                        questionList.add(question);
                    }
                    questionList.add(question);

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.w("tag", e.toString());
            }
            return questionList;
        }

        public static ArrayList<Answer> getAnswerList(String data) {
            ArrayList<Answer> answerList = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(data);
                JSONArray array = object.getJSONArray("answers");
                for (int i = 0; i < array.length(); i++) {
                    Answer answer = new Answer();
                    JSONObject js = array.getJSONObject(i);
                    answer.setId(js.getInt("id"));
                    answer.setContent(js.getString("content"));
                    answer.setDate(js.getString("date"));
                    answer.setAuthorId(js.getInt("authorId"));
                    answer.setAuthorName(js.getString("authorName"));
                    answer.setAuthorAvatar(js.getString("authorAvatar"));
                    answer.setExcitingCount(js.getInt("exciting"));
                    answer.setNaiveCount(js.getInt("naive"));
                    answer.setIs_exciting(js.getBoolean("is_exciting"));
                    answer.setIs_naive(js.getBoolean("is_naive"));
                    answer.setIs_Best(js.getInt("best") == 1);
                    if (js.has("images")) {
                        String images = js.getString("images");
                            answer.setImages(images);
                    }
                    answerList.add(answer);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.w("tag", e.toString());
            }
            return answerList;
        }
            public static String getElement(String data, String name) {
            try {
                return new JSONObject(data).getString(name);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.w("tag", e.toString());
            }
            return null;
        }

}
