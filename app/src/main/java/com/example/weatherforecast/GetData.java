package com.example.weatherforecast;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class GetData {
    private String url;
    private static final String TAG = "ERROR_MSG";

    public GetData(String url) {
        this.url = url;
    }

    public String loadData()  {
        HttpsURLConnection urlConnection = null;
        String result;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpsURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
            urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
            result = getLines(in);
        }
        catch (IOException e){
            Log.e(TAG, "Fail connection", e);
            result = "error";
        }
        finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }

        }
        return result;
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }
}
