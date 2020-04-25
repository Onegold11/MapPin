package com.example.mappin.Thread;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mappin.Map.GPSListener;

import java.net.HttpURLConnection;

public class ReadThread extends DataBaseThread {
    private Context context;

    public ReadThread(Context context) {
        this.context = context;
    }

    @Override
    protected String getRequestMethod() {
        return "GET";
    }

    @Override
    protected String setUrlAddress() {
        return DataBaseThread.server_url + "/read.php";
    }

    @Override
    protected void setUrlParams(HttpURLConnection conn, String... values) {

    }

    @Override
    protected void showRequestResult(int resCode) {
        if(resCode != HttpURLConnection.HTTP_OK){
            Toast.makeText(context, "마커 갱신 실패", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "마커 갱신 성공", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void showDataBaseResult(String value) {
        Log.d("showResult : ", value);
        GPSListener.getInstance().showAllMarker(value);
    }
}
