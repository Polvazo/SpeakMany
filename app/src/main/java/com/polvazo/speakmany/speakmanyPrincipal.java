package com.polvazo.speakmany;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.firebaseChat.model.mensaje;

public class speakmanyPrincipal extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button cambiar;
    private Button buscarChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speakmany_principal);

        buscarChat = (Button)findViewById(R.id.btn_buscar_chat);
        buscarChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(speakmanyPrincipal.this, chateaMucho.class);
                startActivity(i);
            }
        });


        cambiar = (Button)findViewById(R.id.btn_cambiar_usuario);
        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Cambiarusuario();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        String usuarito = Preferencias.obtener(Preferencias.APODO,getApplicationContext());
        if(usuarito==null){
            RegistrarUsuario();
        }
        else{
            Snackbar.make(findViewById(R.id.PrincipalLayout),"Bienvenido, "+usuarito,Snackbar.LENGTH_INDEFINITE).show();

            //Toast.makeText(this, "Hola,  "+ usuarito, Toast.LENGTH_SHORT).show();
        }

    }

    public void RegistrarUsuario(){


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(speakmanyPrincipal.this);
        View mView = getLayoutInflater().inflate(R.layout.registrar_usuario, null);

        final EditText usuario = (EditText)mView.findViewById(R.id.et_registrar_usuario);
        final Button aceptar =  (Button)mView.findViewById(R.id.btn_registrar_usuario);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario_cache=usuario.getText().toString();
                Preferencias.Guardar(Preferencias.APODO,usuario_cache,getApplicationContext());
                Intent inten = new Intent(speakmanyPrincipal.this,speakmanyPrincipal.class);
                startActivity(inten);

                gestionarUser user = new gestionarUser();

                user.crearUSUARIO("id"+Preferencias.obtener(Preferencias.APODO,getApplicationContext()),Preferencias.obtener(Preferencias.APODO,getApplicationContext()));



            }
        });


    }
    public void Cambiarusuario(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(speakmanyPrincipal.this);
        View vista = getLayoutInflater().inflate(R.layout.cambiar_usuario, null);

        final EditText usuario = (EditText)vista.findViewById(R.id.et_cambiar_usuario);
        final TextView mostrar = (TextView)vista.findViewById(R.id.txt_usuario);
        final Button aceptar =  (Button)vista.findViewById(R.id.btn_registrar_usuario_cambio);

        String nombreUsuario= Preferencias.obtener(Preferencias.APODO,getApplicationContext());

        mBuilder.setView(vista);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        mostrar.setText("Su apodo actual es : " + nombreUsuario);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario_cache=usuario.getText().toString();
                Preferencias.Guardar(Preferencias.APODO,usuario_cache,getApplicationContext());
                Intent inten = new Intent(speakmanyPrincipal.this,speakmanyPrincipal.class);
                startActivity(inten);
            }
        });

    }


}
