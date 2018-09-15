package com.example.minseo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);
        Log.e("zmfflr", "ddddd");
        ListView listView = (ListView)findViewById(R.id.sublist);
        Intent intent = getIntent();
        ArrayList<Weather> list =
                (ArrayList<Weather>)intent.getSerializableExtra("subdata");
        ArrayAdapter<Weather> adapter =
                new ArrayAdapter<>(
                        SubActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setDivider(new ColorDrawable(Color.GREEN));
        listView.setDividerHeight(2);

        Button button = (Button)findViewById(R.id.sub);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });


    }
}
