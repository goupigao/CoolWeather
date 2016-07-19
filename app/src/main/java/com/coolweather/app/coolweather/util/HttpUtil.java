package com.coolweather.app.coolweather.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/18.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                //StringBuilder response = null;
                StringBuffer out = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    int responseCode = connection.getResponseCode();
                    InputStream in;
                    if (responseCode == 200) {
                        in = new BufferedInputStream(connection.getInputStream());
                    } else {
                        in = new BufferedInputStream(connection.getErrorStream());
                    }
                    //BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    //response = new StringBuilder();
                    //String line;
                    //while ((line = reader.readLine()) != null) {
                        //response.append(line);
                    //}
                    out = new StringBuffer();
                    byte[] b = new byte[1024];
                    for(int n;(n = in.read(b)) != -1;){
                        out.append(new String(b, 0, n));
                    }
                    if (listener != null) {
                        // 回调onFinish()方法
                        //listener.onFinish(response.toString());
                        listener.onFinish(out.toString());
                    }
                } catch (EOFException e) {
                    if (listener != null && out != null) {
                        listener.onFinish(out.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                        e.printStackTrace();
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
