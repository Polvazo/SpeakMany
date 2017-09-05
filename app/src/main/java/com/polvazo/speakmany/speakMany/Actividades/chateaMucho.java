package com.polvazo.speakmany.speakMany.Actividades;




import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.R;
import com.polvazo.speakmany.speakMany.Modelos.mensaje;
import com.polvazo.speakmany.speakMany.Util.comprobarInternet;
import com.polvazo.speakmany.speakMany.Util.gestionarSalaChat;
import com.polvazo.speakmany.speakMany.Util.gestionarUser;

import com.polvazo.speakmany.speakMany.Util.preferencia;
import com.polvazo.speakmany.speakMany.constantes.constantes;

import java.util.ArrayList;
import java.util.List;


public class chateaMucho extends AppCompatActivity {

    private static final String TAG = chateaMucho.class.getName();

    private EditText metText;
    private Button mbtSent;
    private DatabaseReference mFirebaseRef;
    private List<mensaje> mChats;
    private RecyclerView mRecyclerView;
    private mensajeAdapter mAdapter;
    private String mId;
    FirebaseDatabase database;
    private String SalazaPapu;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatea_mucho);

        mId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        preferencia.Guardar(constantes.IDUSUARIO_CONECTADO,mId,getApplicationContext());
        gestionarUser.crearUsuarioConectado(getApplicationContext());
        BuscarChat();

    }

    public void BuscarChat(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(chateaMucho.this);
        View mView = getLayoutInflater().inflate(R.layout.dialogo_buscar_chat, null);

        final Button aceptar =  (Button)mView.findViewById(R.id.btn_chat);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!comprobarInternet.verificaConexion(chateaMucho.this)){
                    Toast.makeText(getBaseContext(),"Comprueba tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    BuscarChat();
                }
                else{

                    dialog.dismiss();
                    gestionarSalaChat chat = new gestionarSalaChat(chateaMucho.this);
                    chat.buscarNumerodeChat(chateaMucho.this);
                }



            }
        });
        dialog.cancel();
        dialog.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferencia.Elminar(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("CHATEAMUCHO")
                .setMessage("¿Está seguro que desea salir?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        chateaMucho.super.onBackPressed();
                    }
                }).create().show();
    }

    public void nextChat(){

        metText = (EditText) findViewById(R.id.message);
        mbtSent = (Button) findViewById(R.id.btn_send);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvChat);
        mChats = new ArrayList<>();



        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new mensajeAdapter(mChats, mId);
        mRecyclerView.setAdapter(mAdapter);
        database = FirebaseDatabase.getInstance();

        mFirebaseRef = database.getReference().child(constantes.SALA_CHAT_OCUPADO).child(preferencia.obtener(constantes.ID_NUMERO_SALA, chateaMucho.this)).child("mensajes");
        Log.e("sla",preferencia.obtener(constantes.ID_NUMERO_SALA, chateaMucho.this));
        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString();

                if (!message.isEmpty()) {

                    mFirebaseRef.push().setValue(new mensaje(message, mId));
                }

                metText.setText("");
            }
        });


        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {

                        mensaje model = dataSnapshot.getValue(mensaje.class);

                        mChats.add(model);
                        mRecyclerView.scrollToPosition(mChats.size() - 1);
                        mAdapter.notifyItemInserted(mChats.size() - 1);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
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
                Log.d(TAG, databaseError.getMessage());
            }
        });

    }


}
