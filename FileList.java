package com.example.filemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class FileList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list2);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        TextView nofilestxt = findViewById(R.id.nofilestxt);

        String path = getIntent().getStringExtra("path");
        //provides all files and folders in the root
        File root = new File(path);
        File[] filesandFolders = root.listFiles();

        //if else statement, files not found text will be visible if no files are found
        if(filesandFolders==null || filesandFolders.length==0){
            nofilestxt.setVisibility(View.VISIBLE);
            return;
        }

        nofilestxt.setVisibility(View.INVISIBLE);

        //calls adapter file for recyclerview

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new adapter(getApplicationContext(),filesandFolders));

    }
}

