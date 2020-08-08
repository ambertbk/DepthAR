package com.example.AmbersAR;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String[] items = {"Send Text", "Send Image", "Stream Video"};
    private String[] itemDescriptions = {"Click here to send text", "Click here to send image", "Click here to stream video"};
    private int[] icons = {R.drawable.icon_text, R.drawable.icon_img, R.drawable.icon_video};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        System.out.println("*****************************List view is:" + listView);

        ListAdapter listAdapter = new ListAdapter(this, items, itemDescriptions, icons);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent textIntent = new Intent(MainActivity.this, TextActivity.class);
                    startActivity(textIntent);
                }
                if (position == 1){
                    Intent imageIntent = new Intent(MainActivity.this, ImageActivity.class);
                    startActivity(imageIntent);
                }
                if (position == 2){
//                    Toast.makeText(MainActivity.this, "Stream Video", Toast.LENGTH_SHORT).show();
                    Intent videoIntent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(videoIntent);
                }
            }
        });
    }

}