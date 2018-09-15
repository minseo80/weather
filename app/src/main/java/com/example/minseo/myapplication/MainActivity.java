package com.example.minseo.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> cities;
    ArrayAdapter<String> adapter;

    ArrayList<Weather> weathers;

    ProgressDialog progressDialog;

    Handler handler = new Handler(){
        public void handleMessage(Message message){
            if(message.what == 0)
                adapter.notifyDataSetChanged();
            else if(message.what == 1)
                Toast.makeText(
                        MainActivity.this,
                        "다운로드 에러", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    };

    class ThreadEx extends Thread{
        public void run(){
            try{
                DocumentBuilderFactory factory =
                        DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder =
                        factory.newDocumentBuilder();
                Document documet = documentBuilder.parse(
                        "http://www.weather.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108");
                Element root = documet.getDocumentElement();
                NodeList items = root.getElementsByTagName("city");

                cities.clear();
                for(int i=0; i<items.getLength(); i=i+1){
                    Node item = items.item(i);
                    Node city = item.getFirstChild();
                    String content = city.getNodeValue();
                    cities.add(content);
                }

                weathers.clear();

                NodeList items1 = root.getElementsByTagName("tmEf");
                NodeList items2 = root.getElementsByTagName("tmx");
                NodeList items3 = root.getElementsByTagName("tmn");
                NodeList items4 = root.getElementsByTagName("wf");

                for(int i=0; i<items1.getLength(); i=i+1){
                    Weather weather = new Weather();
                    Node node = items1.item(i);
                    Node child = node.getFirstChild();
                    String content = child.getNodeValue();
                    weather.setTmEf(content);

                    node = items2.item(i);
                    child = node.getFirstChild();
                    content = child.getNodeValue();
                    weather.setTmx(content);

                    node = items3.item(i);
                    child = node.getFirstChild();
                    content = child.getNodeValue();
                    weather.setTmn(content);

                    node = items4.item(i+1);
                    child = node.getFirstChild();
                    content = child.getNodeValue();
                    weather.setWf(content);

                    weathers.add(weather);
                }
                Log.e("weathers", weathers.toString());

                handler.sendEmptyMessage(0);

            }
            catch(Exception e){
                Log.e("xml 예외", e.getMessage());
                handler.sendEmptyMessage(1);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cities = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, cities);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.RED));
        listView.setDividerHeight(3);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                progressDialog = ProgressDialog.show(
                        MainActivity.this,
                        "날씨 데이터", "가져오는 중");

                ThreadEx th = new ThreadEx();
                th.start();
            }
        });

        weathers = new ArrayList<>();

        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);

                ArrayList<Weather>subList = new ArrayList<>();
                for(int i=position*13; i<(position+1)*13; i=i+1){
                    subList.add(weathers.get(i));
                }

                intent.putExtra("subdata", subList);
                startActivity(intent);
            }
        });

    }
}
