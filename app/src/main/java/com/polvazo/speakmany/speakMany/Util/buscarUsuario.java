package com.polvazo.speakmany.speakMany.Util;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.polvazo.speakmany.speakMany.Modelos.mensaje;
import com.polvazo.speakmany.speakMany.constantes.constantes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class buscarUsuario {
    private static DatabaseReference mDatabase;
    public static DatabaseReference chat;

    public static void makeUserChat(Context context) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(constantes.IDUSUARIO_CONECTADO).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long allNum = dataSnapshot.getChildrenCount();
                int maxNum = (int)allNum;
                Iterable<DataSnapshot> ds = dataSnapshot.getChildren();
                Iterator<DataSnapshot> ids = ds.iterator();



                int count = 0;

                while(ids.hasNext() && count < 1) {
                    DataSnapshot dataSnapshotChild = ids.next();
                    final mensaje mensaje = dataSnapshotChild.getValue(mensaje.class);



                    List<String> usuarios = new ArrayList<String>();
                    String usuarioParaChatear = ids.next().getValue().toString();
                    usuarios.add(usuarioParaChatear);
                    chat = mDatabase.child(constantes.SALA_CHAT_DISPONIBLE).child(usuarioParaChatear);
                    count ++;
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }}

