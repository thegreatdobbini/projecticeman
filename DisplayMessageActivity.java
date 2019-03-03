package com.deshpande.camerademo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTPClient;
import org.json.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


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
                        "", 21, "/storage/emulated/0/Pictures/ProjectIceman/IMG.jpg", getApplicationContext(), 'd', destName + ".xml");
                Log.d(TAG, "Done.");
                Log.d(TAG, destName);
                if (status == "nf") {
                    Log.d(TAG, "Waiting for file.");

                    do {
                        status = ftpclient.ftpConnect("35.188.206.157", "anonymous",
                                "", 21, "/storage/emulated/0/Pictures/ProjectIceman/IMG.jpg", getApplicationContext(), 'd', destName + ".xml");
                        SystemClock.sleep(300);
                    } while (status == "nf");
                } else if (status == "Error.") {
                    Log.d(TAG, "connection failed");
                }
                Log.d(TAG, "Passed.");

                ftpclient.ftpDisconnect();

                try {
                    File input = new File("/storage/emulated/0/" + destName + ".xml");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(input);
                    doc.getDocumentElement().normalize();
                    Log.d(TAG, "Root Elem: " + doc.getDocumentElement().getNodeName());
                    NodeList nList = doc.getElementsByTagName("team");
                    Node nNode = nList.item(0);
                    Log.d(TAG, "Current Elem:" + nNode.getNodeName());
                    if(nNode.getNodeType() == Node.ELEMENT_NODE){
                        Element eElement = (Element) nNode;
                        String foundStr = eElement.getAttribute("found").trim();

                        if(foundStr.equals("true")){
                            File local = new File("/storage/emulated/0/local.xml");
                            input.renameTo(local);
                            Intent myIntent = new Intent(getBaseContext(), StatsRetrieved.class);
                            startActivity(myIntent);
                            finish();
                        }else {
                            Intent intent = new Intent(getBaseContext(), returnFailed.class);
                            startActivity(intent);
                            finish();
                        }

                        Log.d(TAG, foundStr);
                        Log.d(TAG, eElement.getElementsByTagName("name").item(0).getTextContent());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                }
            }
        }).start();

        Log.d(TAG, "HERE?");

        /*try {
            /*String in = "/storage/emulated/0/" + destName + ".json";
            TextView textView4 = (TextView) findViewById(R.id.textView);

            JSONObject reader = new JSONObject(in);
            JSONObject data = reader.getJSONObject("data");

            String found = data.getString("found");

            textView4.setText(in);

            if (found.equals("true")) {
                startActivity(new Intent(this, StatsRetrieved.class));
//                        finish();
            } else {
                startActivity(new Intent(this, returnFailed.class));
//                        finish();
            }
            Log.d(TAG, "WHERE");
            File input = new File("/storage/emulated/0/" + destName + ".json");
            Log.d(TAG, "WHERE-1");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Log.d(TAG, "WHERE-2");
            Document doc = dBuilder.parse(input);
            doc.getDocumentElement().normalize();
            Log.d(TAG, "WHERE-3");
            Log.d(TAG, "Root Elem: " + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("team");
            Node nNode = nList.item(0);
            Log.d(TAG, "Current Elem:" + nNode.getNodeName());
            if(nNode.getNodeType() == Node.ELEMENT_NODE){
                Element eElement = (Element) nNode;
                Log.d(TAG, eElement.getAttribute("found"));
                Log.d(TAG, doc.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }*/
    }
}
