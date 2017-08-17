package com.polvazo.speakmany.speakMany.Modelos;



/**
 * Created by USUARIO on 31/07/2017.
 */

public class mensaje {
    public String mTexto;
    public String mUsuario;


    public mensaje (){}

    public mensaje(String mTexto, String mUsuario) {
        this.mTexto = mTexto;
        this.mUsuario = mUsuario;
    }

    public String getmTexto() {
        return mTexto;
    }

    public void setmTexto(String mTexto) {
        this.mTexto = mTexto;
    }

    public String getmUsuario() {
        return mUsuario;
    }

    public void setmUsuario(String mUsuario) {
        this.mUsuario = mUsuario;
    }

   }
