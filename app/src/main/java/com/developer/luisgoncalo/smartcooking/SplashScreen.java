package com.developer.luisgoncalo.smartcooking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SplashScreen extends AppCompatActivity {
    //TODO: Se não for buscar nada à API não entra no if do erro porque chega lá antes de alterar a flag

    private String baseUrl = "https://demo3677899.mockable.io/receitas";
    RequestQueue requestQueue; // This is our requests queue to process our HTTP requests.

    private List<Receita> lista_receitas;
    private boolean error = false;

    //Carregar aqui as receitas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(haveNetworkConnection()) {
            requestQueue = Volley.newRequestQueue(this); // This setups up a new request queue which we will need to make HTTP requests.
            getRepoList();

            if(error){
                crash("Ocorreu um algum tipo de erro.");
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
        }else {
            crash("Ocorreu um erro.\nCertifique-se que tem coneção à internet.");
        }
    }


    private void getRepoList() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Create URL
                try {

                    URL githubEndpoint = new URL(baseUrl);

                    // Create connection
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) githubEndpoint.openConnection();

                    if (myConnection.getResponseCode() == 200) {
                        // Success
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);

                        parseJson(jsonReader);
                        if(lista_receitas.size() == 0){
                            crash("Ocorreu um algum tipo de erro.");
                        }
                    } else {
                        // Error handling code goes here
                        error = true;
                    }

                } catch (IOException e) {
                    error = true;
                    e.printStackTrace();
                }
            }
        });
    }

    private void parseJson(JsonReader jsonReader) {
        System.out.println("Chegou Aqui");
        try {
            lista_receitas = new ArrayList<>();
            List<String> array;
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                Receita new_receita = new Receita();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    switch (jsonReader.nextName()){
                        case "id":
                            new_receita.setId(Long.parseLong(jsonReader.nextString()));
                            break;
                        case "nome":
                            new_receita.setNome(jsonReader.nextString());
                            break;
                        case "dificuldade":
                            new_receita.setDificuldade(Integer.parseInt(jsonReader.nextString()));
                            break;
                        case "tempo":
                            new_receita.setTempo(Integer.parseInt(jsonReader.nextString()));
                            break;
                        case "n_ingredientes":
                            new_receita.setN_ingredientes(Integer.parseInt(jsonReader.nextString()));
                            break;
                        case "categoria":
                            new_receita.setCategoria(jsonReader.nextString());
                            break;
                        case "fornecedor":
                            new_receita.setFornecedor(jsonReader.nextString());
                            break;
                        case "imagem":
                            new_receita.setImagem(jsonReader.nextString());
                            break;
                        case "preparacao":
                            jsonReader.beginArray();
                            array = new ArrayList<>();
                            while (jsonReader.hasNext()) {
                                array.add(jsonReader.nextString());
                            }
                            jsonReader.endArray();
                            new_receita.setPreparacao(array);
                            break;
                        case "ingredientes":
                            jsonReader.beginArray();
                            array = new ArrayList<>();
                            while (jsonReader.hasNext()) {
                                array.add(jsonReader.nextString());
                            }
                            jsonReader.endArray();
                            new_receita.setIngredientes(array);
                            break;
                        case "ingredientes_simples":
                            jsonReader.beginArray();
                            array = new ArrayList<>();
                            while (jsonReader.hasNext()) {
                                array.add(jsonReader.nextString());
                            }
                            jsonReader.endArray();
                            new_receita.setIngredientes_simples(array);
                            break;
                        default:
                            jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                lista_receitas.add(new_receita);
            }
            jsonReader.endArray();
        }catch (IOException e){
            error = true;
        }

        System.out.println("Sera que chegou aqui?");

        for (int i=0;i<lista_receitas.size();i++){
            System.out.println(lista_receitas.get(i));
        }
    }

    private void crash(String tipo){
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
    }

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