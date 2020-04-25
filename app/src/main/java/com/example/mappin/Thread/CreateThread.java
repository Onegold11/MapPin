package com.example.mappin.Thread;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateThread extends DataBaseThread {
    private Context context;

    public CreateThread(Context context) {
        this.context = context;
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected String setUrlAddress() {
        return DataBaseThread.server_url + "/create.php";
    }

    @Override
    protected void setUrlParams(HttpURLConnection conn, String... values) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("latitude=").append(values[0]).append("&");
            buffer.append("longitude=").append(values[1]).append("&");
            buffer.append("address=").append(values[2]).append("&");
            buffer.append("user=").append(values[3]);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(buffer.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        }catch (Exception ex){
            Log.d("setUrlParams errer : ", ex.toString());
        }
    }

    @Override
    protected void showRequestResult(int resCode) {
        if(resCode != HttpURLConnection.HTTP_OK){
            Toast.makeText(context, "마커 생성 실패", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "마커 생성 성공", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void showDataBaseResult(String value) {
    }
}
