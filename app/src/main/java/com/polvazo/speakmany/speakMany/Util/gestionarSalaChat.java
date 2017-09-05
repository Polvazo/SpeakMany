package com.polvazo.speakmany.speakMany.Util;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.polvazo.speakmany.speakMany.Actividades.chateaMucho;
import com.polvazo.speakmany.speakMany.constantes.constantes;
import java.util.ArrayList;
import java.util.Random;
import static android.content.ContentValues.TAG;

public  class gestionarSalaChat {

    private static DatabaseReference mDatabase;
    private static DatabaseReference esperandoCHat;
    private static DatabaseReference numeroDeSala;
    ArrayList<String> salaDisponible = new ArrayList<>();
    static ArrayList<String> Usuarios = new ArrayList<>();
    ArrayList<String> salaDisponibleKey = new ArrayList<>();
    ProgressDialog progressDialog;

    String numerodeSala;
    static ValueEventListener evenet;

    com.polvazo.speakmany.speakMany.Actividades.chateaMucho chateaMucho;

    public gestionarSalaChat(com.polvazo.speakmany.speakMany.Actividades.chateaMucho chateaMucho) {
        this.chateaMucho = chateaMucho;
    }

    public void buscarNumerodeChat(final Context context) {

        //inicializo el estado del buscador del chat


        //inicializo el numero de chat

        progressDialog = new ProgressDialog(context);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        numeroDeSala = mDatabase.child(constantes.SALA_CHAT_DISPONIBLE);

        progressDialog.setTitle("Buscando Sala");
        progressDialog.setMessage("Buscando usuario para chatear");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
                chateaMucho.BuscarChat();

            }
        });
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
                        Log.e("sala", user);
                    }

                    if (salaDisponible.isEmpty()) {

                            numerodeSala = preferencia.obtener(constantes.IDUSUARIO_CONECTADO, context);

                            DatabaseReference GETkEY= mDatabase.child(constantes.SALA_CHAT_DISPONIBLE).push();
                            String keySalita = GETkEY.getKey();
                            GETkEY.setValue(numerodeSala);
                            mDatabase.child(constantes.SALA_CHAT_OCUPADO).child(numerodeSala).child(constantes.USUARIOS).push().setValue(numerodeSala);

                            //GUARDO EL NUMERO DE SALA
                            preferencia.Guardar(constantes.ID_NUMERO_SALA, numerodeSala, context);

                            progressDialog.dismiss();

                            Toast.makeText(context, "Se creo una nueva sala", Toast.LENGTH_SHORT).show();

                            EsperarsUsuario(numerodeSala, context, keySalita,numerodeSala);
                            chateaMucho.nextChat();


                    } else {
                        String id = preferencia.obtener(constantes.IDUSUARIO_CONECTADO, context);
                        Random rand = new Random();
                        String Salita = salaDisponible.get(rand.nextInt(salaDisponible.size()));
                        Log.e("Sala Random", Salita);

                        //Buscar el key de la sala para poder eliminarlo
                        int position = -1;
                        for (int i = 0; i < salaDisponible.size(); i++) {
                            if (salaDisponible.get(i).equals(Salita))
                                position = i;
                        }

                        ///////////////////////////////
                        String keySala = salaDisponibleKey.get(position);
                        //se elimina la disponibilidad de la sala

                        mDatabase.child(constantes.SALA_CHAT_DISPONIBLE).child(keySala).removeValue();
                        //mDatabase.child(constantes.SALA_CHAT_OCUPADO).push().setValue(Salita);

                        preferencia.Guardar(constantes.ID_NUMERO_SALA, Salita, context);
                        Log.e("sala", preferencia.obtener(constantes.ID_NUMERO_SALA, context));
                        //se crea la sala de disponibilidad ocupada

                        mDatabase.child(constantes.SALA_CHAT_OCUPADO).child(Salita).child(constantes.USUARIOS).push().setValue(id);

                        Toast.makeText(context, "Se encontró un usuario conectado", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        chateaMucho.nextChat();
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

    public void EsperarsUsuario(final String room, final Context casa, final String roomDelete, final String room2) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        esperandoCHat = mDatabase.child(constantes.SALA_CHAT_OCUPADO).child(room).child(constantes.USUARIOS);
        final ProgressDialog progressDialog2;
        progressDialog2 = new ProgressDialog(casa);
        progressDialog2.setTitle("Esperando Usuario");
        progressDialog2.setMessage("Esperando un usuario que se conecte a la sala... :)");
        progressDialog2.setCancelable(false);
        progressDialog2.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteChat.eliminarDisponibilidadSala(mDatabase,roomDelete);
                deleteChat.eliminarDisponibilidadSalaOcupada(mDatabase,room2);
                progressDialog2.dismiss();
                chateaMucho.BuscarChat();

            }
        });
        progressDialog2.show();
        evenet = esperandoCHat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuarios.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String user = (String) ds.getValue();
                    Usuarios.add(user);
                    Log.e("usuario", user);
                    if (Usuarios.size() == 2) {
                        progressDialog2.dismiss();
                        if (evenet != null) {
                            esperandoCHat.removeEventListener(evenet);
                        }
                        Toast.makeText(casa, "Se encontro usuario, puede comenzar a chatear", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

 }

