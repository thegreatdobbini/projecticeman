package com.deshpande.camerademo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class StatsRetrieved extends AppCompatActivity {


    private static final String TAG = "StatsRetrieved";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_retrieved);
        TextView textView = (TextView) findViewById(R.id.textView);

        try {
            File input = new File("/storage/emulated/0/local.xml");
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
                String teamName = eElement.getElementsByTagName("name").item(0).getTextContent();
                textView.setText(teamName);


                Log.d(TAG, foundStr);
                Log.d(TAG, eElement.getElementsByTagName("name").item(0).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        //textView.setText("AbhiAndroid"); //set text for text view
    }

    public void sendMessage(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}
