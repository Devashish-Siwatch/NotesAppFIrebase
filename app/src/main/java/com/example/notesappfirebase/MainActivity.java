package com.example.notesappfirebase;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    int button_count=1,pos;
    int i =1;
    int  tcount, tcount2;
    DBHelper helper = new DBHelper(this);
    SQLiteDatabase database;
    Boolean del_press, save_press, first_time=true, reopen=false, initStart,edit_path,have_data;
    EditText mess;
    String loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("create");
        del_press=false;
        save_press=true;
        edit_path=false;
        have_data=false;
        tcount=1;
        tcount2=tcount-1;
      /*  if(reopen){
            System.out.println("Reopen1");
            database.execSQL("DROP TABLE IF EXISTS TAB0");
            String sql = "CREATE TABLE TAB0 (_id INTEGER PRIMARY KEY AUTOINCREMENT, NOTE TEXT)";
            database.execSQL(sql);
            String copy = "INSERT INTO TAB0"+"(NOTE) SELECT NOTE FROM TABS";
            database.execSQL(copy);
            System.out.println("Reopen2");
        }*/
        initStart = true;
        View v = null;
        saveData(v);
    }







    public void saveData(View view) {


        ArrayList<String>arrayList = new ArrayList<>();
        ListView show =findViewById(R.id.NoteShow);


        //show.setBackgroundColor(Color.YELLOW);
        /*
        if (button_count>1){
         database.delete("TAB", "_id = ?", new String[]{""+i});
         i++;
       }
        */
        //show.setVisibility(View.GONE);
        if (button_count==1 && first_time==true){
            database = helper.getWritableDatabase();
            first_time =false;
        }

        button_count++;


        if(del_press==false && edit_path==false) {
            mess = (EditText) findViewById(R.id.Note);
            String message = mess.getText().toString();
            if(message.length()==0 && have_data){

                Toast.makeText(this,"Please enter a note", Toast.LENGTH_SHORT).show();
            }
            else {

                System.out.println("Save"+message);
                ContentValues values = new ContentValues();
                values.put("NOTE", message);
                database.insert("TAB" + tcount2, null, values);
                mess.setText("");
            }}

        if(edit_path){
            mess = (EditText) findViewById(R.id.Note);
            String message = mess.getText().toString();
            if(message.length()==0){
                Toast.makeText(this,"Please enter a note", Toast.LENGTH_SHORT).show();
            }
            else{
                ContentValues values = new ContentValues();
                values.put("NOTE", message);
                database.update("TAB"+tcount2,values,"_id = ?", new String[]{loc});
                edit_path=false;
            }}








        Cursor cursor = database.rawQuery("SELECT NOTE FROM TAB"+tcount2, new String[]{});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        do {
            String a = cursor.getString(0);

            arrayList.add(a);
        } while (cursor.moveToNext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        show.setAdapter(adapter);

        /*
     if(del_press==true){
       // database.execSQL(" UPDATE TAB SET COUNT = NULL");
        //ContentValues cv= new ContentValues();
        for(int j=pos;j<=arrayList.size()-1;j++){

            database.execSQL(" UPDATE TAB SET COUNT = "+j +" WHERE COUNT = "+j+1);
        }

        //save_press = false;
        saveData(view);

} */
      /*   database.execSQL(" UPDATE TAB SET COUNT = NULL");
        ContentValues cv= new ContentValues();
        for(int j=pos;j<=arrayList.size();j++){

            database.execSQL(" UPDATE TAB SET COUNT = "+j +" WHERE");
        }

        //database.insert("TAB",null,cv);
*/
        initStart=false;
        del_press=false;

        show.setLongClickable(true);
        show.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayList.size() > 1) {
                    int px = position + 1;
                    database.execSQL("DELETE FROM TAB" + tcount2 + " WHERE _id = " + px);
                    database.execSQL("DROP TABLE IF EXISTS TAB" + tcount);
                    String sql = "CREATE TABLE TAB" + tcount + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, NOTE TEXT)";
                    database.execSQL(sql);

                    String copy = "INSERT INTO TAB" + tcount + "(NOTE) SELECT NOTE FROM TAB" + tcount2;
                    database.execSQL(copy);
                    // tcount++;
                    database.execSQL("DROP TABLE IF EXISTS TAB" + tcount2);
                    tcount++;
                    tcount2++;
                    del_press = true;
                    saveData(view);

                }
                return true;
            }});

        show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter1, View view, int position, long id) {
                int px = position + 1;
                loc = ""+ px;

                //  database.execSQL("DELETE FROM TAB" + tcount2 + " WHERE _id = " + px);
                Cursor cursor = database.rawQuery("SELECT NOTE FROM TAB"+tcount2+" WHERE _id = ?", new String[]{loc});

                cursor.moveToFirst();
                String n = cursor.getString(0);
                mess.setText(n);
                edit_path=true;
                //  ContentValues cv = new ContentValues();
                //  for(int i =1; i<= arrayList.size();i++){
                //     cv.put("COUNT",i);
                //  }database.insert("TAB", null, cv);

              /*  ArrayList<String>arrayList = new ArrayList<>();
             //   ListView show =findViewById(R.id.NoteShow);
               //

                Cursor cursor = database.rawQuery("SELECT NOTE FROM TAB"+tcount, new String[]{});

                if (cursor != null) {
                    cursor.moveToFirst();
                }
                do {
                    String a = cursor.getString(0);

                    arrayList.add(a);
                } while (cursor.moveToNext());*/

                //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.,arrayList);
                //   show.setAdapter(adapter);

            }
        });

        have_data=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Stop");
        String del = "DROP TABLE IF EXISTS TAB0";
        String make ="CREATE TABLE TAB0 (_id INTEGER PRIMARY KEY AUTOINCREMENT, NOTE TEXT)";
        String copy = "INSERT INTO TAB0(NOTE) SELECT NOTE FROM TAB"+tcount2;
        if(tcount2!=0){
            database.execSQL(del);
            database.execSQL(make);
            database.execSQL(copy);
        }

        reopen = true;


    }

    /*     */

}