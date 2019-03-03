package com.deshpande.camerademo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;



public class DisplayMessageActivity extends AppCompatActivity {

    static TextView text;
    Button button;
    FTPClient ftpClient;

    private MyFTPClientFunctions ftpclient = null;
    private static final String TAG = "DisplayMessageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

                ftpclient = new MyFTPClientFunctions();
                String timestamp = new SimpleDateFormat("HHmmssSS").format(new Date());
                final String destName = timestamp;

                new Thread(new Runnable() {
                    public void run() {
                        String status = "";
                        status = ftpclient.ftpConnect("35.188.206.157", "anonymous",
                                "", 21, "/storage/emulated/0/Pictures/ProjectIceman/IMG.jpg", getApplicationContext(), 'u', destName + ".jpg");
                        status = ftpclient.ftpConnect("35.188.206.157", "anonymous",
                                "", 21, "/storage/emulated/0/Pictures/ProjectIceman/IMG.jpg", getApplicationContext(), 'd', destName + ".json");
                        Log.d(TAG, "Done.");
                        Log.d(TAG, destName);
                        if(status == "nf"){
                            Log.d(TAG, "Waiting for file.");

                            do{
                                status = ftpclient.ftpConnect("35.188.206.157", "anonymous",
                                        "", 21, "/storage/emulated/0/Pictures/ProjectIceman/IMG.jpg", getApplicationContext(), 'd', destName + ".json");
                                SystemClock.sleep(300);
                            }while(status == "nf");
                        }else if(status == "Error."){
                            Log.d(TAG, "connection failed");
                        }
                        Log.d(TAG, "Passed.");

                        ftpclient.ftpDisconnect();
                    }
                }).start();

                try {
                    JSONArray arr = new JSONArray("/storage/emulated/0/" + destName + ".json");
                    String found = arr.getJSONArray(0).getString("found");

                    if (found == "true") {
                        startActivity(new Intent(this, StatsRetrieved.class));
                        finish();
                    } else {
                        startActivity(new Intent(this, returnFailed.class));
                        finish();
                    }
                }
                catch(JSONException e){

                }
    }
}
