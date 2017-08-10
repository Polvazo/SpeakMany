package com.polvazo.speakmany.speakMany.Actividades;


import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.polvazo.speakmany.R;
import com.polvazo.speakmany.speakMany.Util.gestionarUser;



public class chateaMucho extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatea_mucho);




    }



    @Override
    protected void onStart() {
        super.onStart();
        BuscarChat();
    }



    public void BuscarChat (){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(chateaMucho.this);
        View mView = getLayoutInflater().inflate(R.layout.dialogo_buscar_chat, null);

        final Button aceptar =  (Button)mView.findViewById(R.id.btn_chat);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gestionarUser.crearUsuarioConectado(getApplicationContext());
                dialog.dismiss();
            }
        });
        dialog.show();



    }

}
