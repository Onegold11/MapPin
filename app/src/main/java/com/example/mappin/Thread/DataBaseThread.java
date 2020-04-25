package com.example.mappin.Thread;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class DataBaseThread extends AsyncTask<String, String, String> {
    public static final String server_url = "http://WJstorage.asuscomm.com/map";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        final StringBuilder output = new StringBuilder();
        int resCode = 0;

        try{
            URL url = new URL(setUrlAddress());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(conn != null){
                // 연결, 읽기 대기 시간 설정, 5초
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                // POST or GET
                conn.setRequestMethod(getRequestMethod());
                conn.connect();

                //url 변수 설정
                setUrlParams(conn, strings);

                // 웹서버에 페이지 요청
                resCode = conn.getResponseCode();
                Log.d("ResCode : ", resCode+"");

                if(resCode != HttpURLConnection.HTTP_OK)
                    return null;

                // 결과 읽기
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = null;
                while(true){
                    //한줄씩읽음
                    line = reader.readLine();
                    if(line == null){
                        break;
                    }
                    output.append(line + "\n");
                }
                reader.close();
                conn.disconnect();
            }
        }catch (Exception ex){
            Log.d("Connection errer ", ex.toString());
        }

        publishProgress(output.toString(), Integer.toString(resCode));
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        showDataBaseResult(values[0]);
        showRequestResult(Integer.parseInt(values[1]));
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    protected  abstract String getRequestMethod();
    protected abstract String setUrlAddress();
    protected abstract void setUrlParams(HttpURLConnection conn, String... values);
    protected abstract void showRequestResult(int resCode);
    protected abstract void showDataBaseResult(String value);
}
