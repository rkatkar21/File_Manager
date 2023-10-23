package com.example.filemanager;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.os.FileUtils;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

import javax.xml.transform.Result;


public class adapter extends RecyclerView.Adapter<adapter.Viewholder>{

    Context context;
    File[] filesandFolders;

    //constructor
    public adapter(Context context, File[] filesandFolders){

        this.context = context;
        this.filesandFolders = filesandFolders;

    }


    //define methods
    @Override
    public Viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //lets recycler know of item
        View view = LayoutInflater.from(context).inflate(R.layout.recycleritem,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(adapter.Viewholder viewholder, int j) {
        //defines which file to take based on position j
        File selectedFile = filesandFolders[j];
        viewholder.textView.setText(selectedFile.getName());
        //if it is directory, sets folder icon, if file, sets driver file icon
        if(selectedFile.isDirectory()){
            viewholder.imageView.setImageResource(R.drawable.baseline_folder_24);
        }else{
            viewholder.imageView.setImageResource(R.drawable.baseline_insert_drive_file_24);
        }

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedFile.isDirectory()){
                    Intent intent = new Intent(context, FileList.class);
                    String path = selectedFile.getAbsolutePath();
                    intent.putExtra("path", path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else {
                    //if it is file, this opens the file
                    try {
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        String type = "image/*";
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(context.getApplicationContext(), "Unable to open file!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //long click listener to open menu for move, copy, delete, etc.
        //activates if you long click
        viewholder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenu().add("Copy");
                popupMenu.getMenu().add("Delete");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                         @Override
                                                         public boolean onMenuItemClick(MenuItem menuItem) {
                                                             if (menuItem.getTitle().equals("Copy")) {
                                                                 //copies menu item

                                                                 // create a new file in the same folder with a new name
                                                                 File newFile = new File(selectedFile.getParentFile(), "Copy of " + selectedFile.getName());
                                                                 try {
                                                                     // copy the contents of the selected file to the new file
                                                                     InputStream in = new FileInputStream(selectedFile);
                                                                     OutputStream out = new FileOutputStream(newFile);
                                                                     byte[] buffer = new byte[1024];
                                                                     int length;
                                                                     while ((length = in.read(buffer)) > 0) {
                                                                         out.write(buffer, 0, length);
                                                                     }
                                                                     in.close();
                                                                     out.close();
                                                                     Toast.makeText(context.getApplicationContext(), "File copied to " + newFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                                                 } catch (IOException e) {
                                                                     Toast.makeText(context.getApplicationContext(), "Unable to copy file!", Toast.LENGTH_SHORT).show();
                                                                 }
                                                             }


                                                             if (menuItem.getTitle().equals("Delete")) {
                                                                 //deletes menu item
                                                                 boolean deleted = selectedFile.delete();
                                                                 if (deleted) {
                                                                     Toast.makeText(context.getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                                     view.setVisibility(view.GONE);
                                                                 }
                                                             }

                                                             return true;
                                                         }
                                                     });

                            popupMenu.show();
                            return true;

            }
        });


    }

    @Override
    public int getItemCount() {
        return filesandFolders.length;
    }

    //Viewholder

    public class Viewholder extends RecyclerView.ViewHolder{

        //reference of items
        TextView textView;
        ImageView imageView;

        public Viewholder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.filename);
            imageView = itemView.findViewById(R.id.iconview);
        }
    }

}
