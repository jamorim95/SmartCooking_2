package com.developer.luisgoncalo.smartcooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


import java.io.Serializable;
import java.util.List;

import utils.GetReceitasTask;
import utils.ObjectSerializer;

public class SplashScreen extends AppCompatActivity {

    private List<Receita> lista_receitas;
    private boolean error = false;

    private String PREFS_NAME = "SmartCooking_PrefsName";
    private String PREFS_LISTA_RECEITAS = "SmartCooking_lista_receitas";


    //Carregar aqui as receitas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(haveNetworkConnection()) { // Have internet connection

            final GetReceitasTask myTask = new GetReceitasTask(this);
            try {
                myTask.execute().get();
                //List<Receita> lista = myTask.getLista_receitas();
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(PREFS_LISTA_RECEITAS, ObjectSerializer.serialize((Serializable)myTask.getLista_receitas())).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }


            int SPLASH_TIME_OUT = 2000;
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }


    /*private void crash(String tipo){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SplashScreen.this);
        // set title
        alertDialogBuilder.setTitle("Erro");
        // set dialog message
        alertDialogBuilder
                .setMessage(tipo)
                .setCancelable(false)
                .setPositiveButton("Fechar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        SplashScreen.this.finish();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }*/

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}