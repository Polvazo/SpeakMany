package com.polvazo.speakmany.speakMany.Actividades;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.R;
import com.polvazo.speakmany.speakMany.Modelos.mensaje;
import com.polvazo.speakmany.speakMany.Service.VerificarInternet;
import com.polvazo.speakmany.speakMany.Util.deleteChat;
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
    public int estado = 0;
    private AlertDialog dialog1;
    private boolean mSnackbarShown;
    private Snackbar mSnackbar;
    private ChildEventListener evento;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatea_mucho);

        mId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        preferencia.Guardar(constantes.IDUSUARIO_CONECTADO, mId, getApplicationContext());
        gestionarUser.crearUsuarioConectado(getApplicationContext());
        BuscarChat();

    }


    public void BuscarChat() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(chateaMucho.this);
        View mView = getLayoutInflater().inflate(R.layout.dialogo_buscar_chat, null);
        estado=1;
        final Button aceptar = (Button) mView.findViewById(R.id.btn_chat);
        final Button salir = (Button) mView.findViewById(R.id.btn_chat_cancelar);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VerificarInternet tarea =
                        new VerificarInternet(chateaMucho.this, new VerificarInternet.EntoncesHacer() {
                            @Override
                            public void cuandoHayInternet() {
                                // abrimos la nueva ventana.. este es el ELSE de tu if
                                dialog.dismiss();
                                estado = 0;
                                gestionarSalaChat chat = new gestionarSalaChat(chateaMucho.this);
                                chat.buscarNumerodeChat(chateaMucho.this);
                            }

                            @Override
                            public void cuandoNOHayInternet() {
                                Toast.makeText(getBaseContext(), R.string.a_cha_toast_internet, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                BuscarChat();
                            }
                        });
                tarea.execute();

            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dialog.cancel();
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.a_cha_dialogoSalir_title)
                .setMessage(R.string.a_cha_dialogoSalir_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        chateaMucho.super.onBackPressed();
                    }
                }).create().show();
    }

    public void nextChat() {

        metText = (EditText) findViewById(R.id.message);
        mbtSent = (Button) findViewById(R.id.btn_send);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvChat);
        mChats = new ArrayList<>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        //  mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new mensajeAdapter(mChats, mId);

        mRecyclerView.setAdapter(mAdapter);
        database = FirebaseDatabase.getInstance();
        Log.e("sla", preferencia.obtener(constantes.ID_NUMERO_SALA, chateaMucho.this));
        metText.setEnabled(true);
            mFirebaseRef = database.getReference().child(constantes.SALA_CHAT_OCUPADO).child(preferencia.obtener(constantes.ID_NUMERO_SALA, chateaMucho.this)).child("mensajes");

        mFirebaseRef.push().setValue(constantes.ESTADO);

        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString().trim();

                if (message.length() != 0) {

                    mFirebaseRef.push().setValue(new mensaje(message, mId));
                }
                metText.setText("");
            }
        });


       evento = mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {

                        mensaje model = dataSnapshot.getValue(mensaje.class);
                        model.getmTexto();
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


                if (estado == 1) {
                    Log.e("estado", String.valueOf(estado));
                    //Toast toast = Toast.makeText(chateaMucho.this, "Salió de la sala", Toast.LENGTH_SHORT);
                    //toast.show();
                    Log.e("estado", String.valueOf(estado));
                    estado = 1;
                } else {
                    mSnackbar = Snackbar.make(findViewById(R.id.chat), R.string.a_cha_snackbar_userDisconect, Snackbar.LENGTH_INDEFINITE);
                    mSnackbar.setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            mSnackbarShown = false;
                            mSnackbar = null;
                        }

                    });
                    mSnackbar.show();
                    mSnackbarShown = true;
                    Log.e("estado", String.valueOf(estado));
                    //Toast.makeText(chateaMucho.this, "El usuario se desconecto, busque otro usuario", Toast.LENGTH_SHORT).show();
                    metText.setEnabled(false);
                    metText.setHint(R.string.a_cha_editText_hint);

                }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.buscar_otra_vez:

                final AlertDialog.Builder builder = new AlertDialog.Builder(chateaMucho.this);
                builder.setTitle(R.string.a_cha_dialogoBuscar_title);
                builder.setMessage(R.string.a_cha_dialogoBuscar_message);
                builder.setPositiveButton(R.string.a_cha_dialogoBuscar_buttomOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        estado = 1;
                        Log.e("estado", String.valueOf(estado));
                        EliminarSala();
                        BuscarChat();
                        mFirebaseRef.removeEventListener(evento);

                    }
                });
                builder.setNegativeButton(R.string.a_cha_dialogoBuscar_buttomCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                dialog1 = builder.create();
                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                dialog1.show();


            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void EliminarSala() {
        DatabaseReference mdatabase;
        mdatabase = FirebaseDatabase.getInstance().getReference();
        String Sala = preferencia.obtener(constantes.ID_NUMERO_SALA, chateaMucho.this);
        deleteChat.eliminarDisponibilidadSalaOcupada(mdatabase, Sala);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DatabaseReference mdatabase1;
        mdatabase1 = FirebaseDatabase.getInstance().getReference();
        String Sala = preferencia.obtener(constantes.ID_KEY_NUMERO_SALA, chateaMucho.this);
        EliminarSala();

        if (Sala != null) {
            deleteChat.eliminarDisponibilidadSala(mdatabase1, Sala);
        } else {
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mSnackbarShown) {

                Rect sRect = new Rect();
                mSnackbar.getView().getHitRect(sRect);

                //This way the snackbar will only be dismissed if
                //the user clicks outside it.
                if (!sRect.contains((int) ev.getX(), (int) ev.getY())) {
                    mSnackbar.dismiss();
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}
