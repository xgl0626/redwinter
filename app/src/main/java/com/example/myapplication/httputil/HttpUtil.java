package com.example.myapplication.httputil;


import android.os.Handler;


import com.example.myapplication.config.apiconfig;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.Response;

import static com.qiniu.android.common.FixedZone.zone0;


public class HttpUtil {
    public  static String ak = "O1jKGKbtCsRKhB01A_JSf7rEgoJLkWMGPdNmydo6";
    public  static String sk = "8hB_QPdog6gDVF3WGZOOPcjEh02NjmqRIB2bduL2";
    public  static String bucket = "xglbihu";

    public static void loadImage(String address, Callback callback) {
        if (address.endsWith("/"))
            address = address.substring(0, address.length() - 1);
        String name = address.substring(address.lastIndexOf('/') + 1);
        File file = new File(MyApplication.getContext().getExternalCacheDir(), name);
        if (file.exists()) {
            //文件存在则在文件中读取
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] temp = new byte[1024];
                while (inputStream.read(temp) != -1)
                    outputStream.write(temp);
                callback.onResponse(new Response(outputStream.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
                callback.onFail(e);
            } finally {
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFail(e);
                    }
            }
        } else {
            //文件不存在则在网络中读取
            sendHttpRequest(address, null, callback);
        }
    }
    public static void uploadImage(final byte[] data, final String name, final String param, final String address) {
        Auth auth = Auth.create(ak, sk);
        String token = auth.uploadToken(bucket);
                upload(data, name, param, address, token);
    }
    private static void upload(byte[] data, String name, final String param, final String address, String token) {
        Configuration configuration= new Configuration.Builder().zone(zone0)
                .build();
        UploadManager uploadManager = new UploadManager(configuration);
        uploadManager.put(data, name, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    sendHttpRequest(address, param, new Callback() {
                        @Override
                        public void onResponse(Response response) {
                            if (response.isSuccess())
                            {
                                ToastUtils.showHint("图片上传成功");
                            }
                            else
                                ToastUtils.showHint(response.message());
                        }

                        @Override
                        public void onFail(Exception e) {
                            ToastUtils.showError(e.toString());
                        }
                    });
                } else
                ToastUtils.showError(info.error);
            }
        }, null);
    }
        public static void sendHttpRequest(String address, String param) {
            sendHttpRequest(address, param, new Callback() {
                @Override
                public void onResponse(Response response) {

                }

                @Override
                public void onFail(Exception e){
                    ToastUtils.showError(e.toString());
                }
            });
        }
    public static void sendHttpRequest(final String address, final String param, final Callback callback) {
       if(!NetWorkUtils.isNetworkConnected(MyApplication.getContext()))
       {
            ToastUtils.showError("无网络连接");
       }
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(5 * 1000);
                    connection.setConnectTimeout(10 * 1000);
                    if (param == null)
                        connection.setRequestMethod("GET");
                    else {
                        connection.setRequestMethod("POST");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        OutputStream os = connection.getOutputStream();
                        os.write(param.getBytes());
                        os.flush();
                        os.close();
                    }
                    if (connection.getResponseCode() == 200) {
                        final byte[] temp = read(connection.getInputStream());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResponse(new Response(temp));
                            }
                        });
                    } else throw new Exception("无法连接服务器");
                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(e);
                        }
                    });
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }
    private static byte[] read(InputStream is) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int len;
        while ((len = is.read(temp)) != -1)
            outputStream.write(temp, 0, len);
        is.close();
        return outputStream.toByteArray();
    }

    public interface Callback {
        void onResponse(Response response);

        void onFail(Exception e);
    }

    public static class Response {
        private int mStatus;
        private String mInfo;
        private byte[] mData;

        Response(byte[] response) {
                String rawData = new String(response);
            if (rawData != null && rawData.startsWith("\ufeff")) {
                rawData = rawData.substring(1);
            }
            mInfo = Json.getElement(rawData, "info");
            if (mInfo == null) {
                mStatus = 200;
                mData = response;
            } else {
                mStatus = Integer.parseInt(Json.getElement(rawData, "status"));
                if (Json.getElement(rawData, "data") != null)
                    mData = Json.getElement(rawData, "data").getBytes();
                else mData = null;
            }
        }

        public int getStatusCode() {
            return mStatus;
        }

        public boolean isSuccess() {
            return mStatus == 200;
        }

        public String getInfo() {
            return mInfo;
        }

        public String message() {
            return "status:" + mStatus + "\ninfo:" + mInfo;
        }

        public String bodyString() {
            return new String(mData);
        }

        public byte[] bodyBytes() {
            return mData;
        }
    }

}

