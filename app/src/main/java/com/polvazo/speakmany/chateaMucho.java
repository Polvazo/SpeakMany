package com.polvazo.speakmany;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class chateaMucho extends AppCompatActivity {

    private String username;
    private DatabaseReference child;
    private EditText et_message;
    private Button btn_send;
    private String key;
    private String child_username;
    private String child_value;
    private ListView listview;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatea_mucho);
         onBindView();



         child = FirebaseDatabase.getInstance().getReference().child("chat");
         list = new ArrayList<String>();
         adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,list);
         listview.setAdapter(adapter);



         btn_send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Map<String,Object> map = new HashMap<String,Object>();
                 key = child.push().getKey();
                 map.put(key,"");
                 child.updateChildren(map);

                 DatabaseReference message_key = child.child(key);
                 Map<String,Object> map2 = new HashMap<String,Object>();
                 map2.put("username",Preferencias.obtener(Preferencias.APODO,getApplicationContext()));
                 map2.put("msg",et_message.getText().toString());
                 message_key.updateChildren(map2);

                 et_message.setText("");
             }
         });
    }
    @Override
    protected void onResume() {
        super.onResume();
        child.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onGetChild(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                onGetChild(dataSnapshot);


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("databaseError",databaseError.toString());
                Toast.makeText(chateaMucho.this,"onCancelled",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        child = FirebaseDatabase.getInstance().getReference();
        child.child("chat").removeValue();
    }

    private void onBindView() {
        listview = (ListView)findViewById(R.id.list_view);
        et_message = (EditText) findViewById(R.id.message);
        btn_send = (Button) findViewById(R.id.btn_send);
    }




    private void onGetChild(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            child_value = (String) ((DataSnapshot)i.next()).getValue();
            child_username = (String) ((DataSnapshot)i.next()).getValue();
            list.add(child_username+" : "+child_value);
        }
        adapter.notifyDataSetChanged();



    }

}
