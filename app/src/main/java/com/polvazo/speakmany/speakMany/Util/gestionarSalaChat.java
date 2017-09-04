package com.polvazo.speakmany.speakMany.Util;


import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


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
    private static DatabaseReference esperandoCHat;
    private static DatabaseReference numeroDeSala;
    static ArrayList<String> salaDisponible = new ArrayList<>();
    static ArrayList<String> Usuarios = new ArrayList<>();
    static ArrayList<String> salaDisponibleKey = new ArrayList<>();
    static ProgressDialog progressDialog;

    static String numerodeSala;
    static ChildEventListener evenet;



    public static void buscarNumerodeChat(final Context context) {

        //inicializo el estado del buscador del chat


        //inicializo el numero de chat

        progressDialog = new ProgressDialog(context);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        numeroDeSala = mDatabase.child(constantes.SALA_CHAT_DISPONIBLE);

        progressDialog.setTitle("Buscando Sala");
        progressDialog.setMessage("Buscando usuario para chatear");
        progressDialog.show();




        numeroDeSala.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    salaDisponible.clear();
                    salaDisponibleKey.clear();
                    //Insertar en un arrayList todas las salas disponibles
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String user = (String) ds.getValue();
                        String key = ds.getKey();
                        salaDisponible.add(user);
                        salaDisponibleKey.add(key);
                        Log.e("sala",user);
                    }

                    if (salaDisponible.isEmpty()) {

                        numerodeSala= preferencia.obtener(constantes.IDUSUARIO_CONECTADO, context);
                        mDatabase.child(constantes.SALA_CHAT_DISPONIBLE).push().setValue(numerodeSala);
                        mDatabase.child(constantes.SALA_CHAT_OCUPADO).child(numerodeSala).child(constantes.USUARIOS).push().setValue(numerodeSala);

                        //GUARDO EL NUMERO DE SALA
                        preferencia.Guardar(constantes.ID_NUMERO_SALA,numerodeSala,context);

                        progressDialog.dismiss();

                        Toast.makeText(context, "Se creo una nueva sala", Toast.LENGTH_SHORT).show();

                        EsperarsUsuario(numerodeSala,context);






                    }
                    else {
                        String id= preferencia.obtener(constantes.IDUSUARIO_CONECTADO, context);
                        Random rand = new Random();
                        String Salita = salaDisponible.get(rand.nextInt(salaDisponible.size()));
                        Log.e("Sala Random", Salita);
                        Toast.makeText(context, "Se encontró un usuario conectado", Toast.LENGTH_SHORT).show();

                        //Buscar el key de la sala para poder eliminarlo
                        int position = -1;
                        for (int i = 0; i < salaDisponible.size(); i++) {
                            if (salaDisponible.get(i) == Salita)
                                position = i;
                        }

                        ///////////////////////////////
                        String keySala = salaDisponibleKey.get(position);
                        //se elimina la disponibilidad de la sala

                        mDatabase.child(constantes.SALA_CHAT_DISPONIBLE).child(keySala).removeValue();
                        //mDatabase.child(constantes.SALA_CHAT_OCUPADO).push().setValue(Salita);

                        preferencia.Guardar(constantes.ID_NUMERO_SALA,Salita,context);
                        Log.e("sala",preferencia.obtener(constantes.ID_NUMERO_SALA,context));
                        //se crea la sala de disponibilidad ocupada
                        mDatabase.child(constantes.SALA_CHAT_OCUPADO).child(Salita).child(constantes.USUARIOS).push().setValue(id);
                        //mDatabase.child(constantes.SALA_CHAT_OCUPADO).push().setValue(Salita);

                        progressDialog.dismiss();
                    }

                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    public static void EsperarsUsuario(String sala, final Context casa){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        esperandoCHat =  mDatabase.child(constantes.SALA_CHAT_OCUPADO).child(sala).child(constantes.USUARIOS);
        final ProgressDialog progressDialog2;
        progressDialog2 = new ProgressDialog(casa);
        progressDialog2.setTitle("Esperando Usuario");
        progressDialog2.setMessage("Esperando Usuario que se conecte");
        progressDialog2.show();
        esperandoCHat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuarios.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String user = (String) ds.getValue();
                    Usuarios.add(user);
                    Log.e("usuario",user);
                        if(Usuarios.size()==2){
                            progressDialog2.dismiss();
                            Toast.makeText(casa, "Se encontre usuario", Toast.LENGTH_SHORT).show();
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

 }

