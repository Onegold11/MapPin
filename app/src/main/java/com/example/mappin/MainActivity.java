package com.example.mappin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String server_url = "http://WJstorage.asuscomm.com/map";
    Button btn_create;
    Button btn_read;
    Button btn_update;
    Button btn_delete;
    TextView textView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_create = findViewById(R.id.btn_create);
        btn_read = findViewById(R.id.btn_read);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        textView = findViewById(R.id.txt_log);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateThread thread = new CreateThread();
                thread.start();
            }
        });
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadThread thread = new ReadThread();
                thread.start();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateThread thread = new UpdateThread();
                thread.start();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteThread thread = new DeleteThread();
                thread.start();
            }
        });
    }

    class CreateThread extends Thread{
        @Override
        public void run() {
            final StringBuilder output = new StringBuilder();
            try{
                URL url = new URL(server_url + "/create.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn != null){
                    // 연결 대기 시간 설정, 10초
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.connect();

                    //url 설정
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("latitude=").append("100.1").append("&");
                    buffer.append("longitude=").append("20.2").append("&");
                    buffer.append("address=").append("테스트장소").append("&");
                    buffer.append("user=").append("test100");

                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(buffer.toString().getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    // 웹서버에 페이지 요청
                    int resCode = conn.getResponseCode();
                    Log.d("ResCode : ", resCode+"");
                    if(resCode != HttpURLConnection.HTTP_OK)
                        return;

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
                Log.d("Connection errer : ", ex.getMessage());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(output.toString());
                }
            });
        }
    }
    class ReadThread extends Thread{
        @Override
        public void run() {
            final StringBuilder output = new StringBuilder();
            try{
                URL url = new URL(server_url + "/read.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn != null){
                    // 연결 대기 시간 설정, 10초
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    // 웹서버에 페이지 요청
                    int resCode = conn.getResponseCode();
                    Log.d("ResCode : ", resCode+"");
                    if(resCode != HttpURLConnection.HTTP_OK)
                        return;

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
                Log.d("Connection errer : ", ex.getMessage());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(output.toString());
                }
            });
        }
    }
    class UpdateThread extends Thread{
        @Override
        public void run() {
            final StringBuilder output = new StringBuilder();
            try{
                URL url = new URL(server_url + "/update.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn != null){
                    // 연결 대기 시간 설정, 10초
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.connect();

                    //url 설정
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("id=").append("5").append("&");
                    buffer.append("address=").append("테스트장소1");

                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(buffer.toString().getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    // 웹서버에 페이지 요청
                    int resCode = conn.getResponseCode();
                    Log.d("ResCode : ", resCode+"");
                    if(resCode != HttpURLConnection.HTTP_OK)
                        return;

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
                Log.d("Connection errer : ", ex.getMessage());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(output.toString());
                }
            });
        }
    }
    class DeleteThread extends Thread{
        @Override
        public void run() {
            final StringBuilder output = new StringBuilder();
            try{
                URL url = new URL(server_url + "/delete.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn != null){
                    // 연결 대기 시간 설정, 10초
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.connect();

                    //url 설정
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("id=").append("5");

                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(buffer.toString().getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    // 웹서버에 페이지 요청
                    int resCode = conn.getResponseCode();
                    Log.d("ResCode : ", resCode+"");
                    if(resCode != HttpURLConnection.HTTP_OK)
                        return;

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
                Log.d("Connection errer : ", ex.getMessage());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(output.toString());
                }
            });
        }
    }
}
