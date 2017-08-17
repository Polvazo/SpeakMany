package com.polvazo.speakmany.speakMany.Util;


import android.content.Context;
import android.util.Log;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.polvazo.speakmany.speakMany.constantes.constantes;

import java.util.ArrayList;

import java.util.Random;

import static android.content.ContentValues.TAG;

public  class gestionarSalaChat {

    private static DatabaseReference mDatabase;
    private static DatabaseReference numeroDeSala;
    static ArrayList<String> salaDisponible=new ArrayList<>();


    public static void crearChat (Context context){


        //se crea la sala de chat para que otro usuario puedea conectarse
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String salachat = preferencia.obtener(constantes.IDUSUARIO_CONECTADO, context);
        //mDatabase.child("salaChatDisponible").push().setValue(salachat);
        //inserto el usuario en la sala de chat
        mDatabase.child(constantes.SALA_CHAT_DISPONIBLE).child(salachat).child(constantes.USUARIOS).push().setValue(salachat);

    }

    public static String buscarNumerodeChat (Context context){

        //inicializo el estado del buscador del chat

        String Sala;
        //inicializo el numero de chat


        mDatabase = FirebaseDatabase.getInstance().getReference();
        numeroDeSala = mDatabase.child(constantes.SALA_CHAT_DISPONIBLE);


        numeroDeSala.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    salaDisponible.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        String user= (String) ds.getValue();
                        salaDisponible.add(user);
                    }


                }catch (Exception ex){
                    Log.e(TAG, ex.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* numeroDeSala.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try {
                    salaDisponible.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        String user= (String) ds.getValue();
                        salaDisponible.add(user);
                    }


                }catch (Exception ex){
                    Log.e(TAG, ex.getMessage());
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        if(salaDisponible.isEmpty())
        {

             Sala = preferencia.obtener(constantes.IDUSUARIO_CONECTADO, context);
            mDatabase.child(constantes.SALA_CHAT_DISPONIBLE).child(Sala).child(constantes.USUARIOS).push().setValue(Sala);
            return Sala;
        }
        else
        {
            Random rand = new Random();
            String Salita = salaDisponible.get(rand.nextInt(salaDisponible.size()));
            mDatabase.child(constantes.SALA_CHAT_DISPONIBLE).child(Salita).removeValue();
            return Salita;
        }

    }



}
