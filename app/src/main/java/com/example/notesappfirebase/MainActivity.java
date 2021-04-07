package com.example.notesappfirebase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    int button_count=1,pos;
    int i =1;
    int  tcount, tcount2;
    String del;
    ListView show;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> docData = new HashMap<>();
    Boolean del_press, save_press, first_time=true, reopen=false, initStart,edit_path,have_data;
    EditText mess;
    String loc;
    ArrayList<String>arrayList = new ArrayList<>();
    ArrayList<String>idList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         show=findViewById(R.id.NoteShow);

        System.out.println("create");
        del_press=false;
        save_press=true;
        edit_path=false;
        have_data=false;
        tcount=1;
        tcount2=tcount-1;
        adapter=  new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        show.setAdapter(adapter);
        show.setLongClickable(true);
        show.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayList.size() > 1) {
                    int px = position + 1;
                    del = (String) idList.get(position);
                    Log.d("Del",del);
                    arrayList.remove(position);



                    db.collection("name").document(del)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Del", "DocumentSnapshot successfully deleted!"+del);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("DEL", "Error deleting document", e);
                                }
                            });
                    //    arrayList.remove("del");
                    //      adapter.notifyDataSetChanged();
                    tcount++;
                    tcount2++;
                    //del_press = true;

                }
                return true;
            }});
        db.collection("name")
                .orderBy("Testing", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                            idList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData().get("Testing"));
                                String a = document.getData().get("Testing").toString();
                                arrayList.add(a);
                                idList.add(document.getId());

                            }  adapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        Log.d("ARRAY", String.valueOf(arrayList.size()));


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

    }







    public void saveData(View view) {




        //show.setBackgroundColor(Color.YELLOW);
        /*
        if (button_count>1){
         database.delete("TAB", "_id = ?", new String[]{""+i});
         i++;
       }
        */
        //show.setVisibility(View.GONE);
 

        button_count++;


        if(del_press==false && edit_path==false) {
            mess = (EditText) findViewById(R.id.Note);
            String message = mess.getText().toString();
            if(message.length()==0){

                Toast.makeText(this,"Please enter a note", Toast.LENGTH_SHORT).show();
            }
            else {

                System.out.println("Save"+message);
                docData.put("Testing", message);
                db.collection("name").add(docData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>(){

                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("WORKS", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Does not WORK :(", "Error adding document", e);
                            }
                        });
                mess.setText("");
            }}
/*
        if(edit_path){
            mess = (EditText) findViewById(R.id.Note);
            String message = mess.getText().toString();
            if(message.length()==0){
                Toast.makeText(this,"Please enter a note", Toast.LENGTH_SHORT).show();
            }
            else{
                //TODO Adding editing option
             //   ContentValues values = new ContentValues();
           //     values.put("NOTE", message);
             //   database.update("TAB"+tcount2,values,"_id = ?", new String[]{loc});
            //    edit_path=false;
            }}

*/

        db.collection("name")
                .orderBy("Testing", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                            idList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData().get("Testing"));
                                String a = document.getData().get("Testing").toString();
                                arrayList.add(a);
                                idList.add(document.getId());

                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        Log.d("ARRAY", String.valueOf(arrayList.size()));



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
/*

        show.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayList.size() > 1) {
                    int px = position + 1;
                    del = (String) idList.get(position);
                    Log.d("Del",del);
                    arrayList.remove(position);



                    db.collection("name").document(del)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Del", "DocumentSnapshot successfully deleted!"+del);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("DEL", "Error deleting document", e);
                                }
                            });
                //    arrayList.remove("del");
              //      adapter.notifyDataSetChanged();
                    tcount++;
                    tcount2++;
                    del_press = true;

                }
                return true;
            }});
*/
        /* TODO Adding edit option
        show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<? > adapter1, View view, int position, long id) {
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
                } while (cursor.moveToNext());

                //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.,arrayList);
                //   show.setAdapter(adapter);

            }
        });
  */
       // have_data=true;
    }
}