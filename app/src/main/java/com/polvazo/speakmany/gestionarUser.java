package com.polvazo.speakmany;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polvazo.speakmany.firebaseChat.model.mensaje;

import static android.R.attr.name;

/**
 * Created by USUARIO on 31/07/2017.
 */

public class gestionarUser {

    private DatabaseReference mDatabase;

    public void crearUSUARIO(String UserId, String name) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("usuarios").child(UserId).setValue(name);
    }

    public void editarUSUARIO(String UserId, String name){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("usuarios").child(UserId).push().setValue(name);
    }
}
