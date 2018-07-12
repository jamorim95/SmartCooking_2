package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.developer.luisgoncalo.smartcooking.Receita;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import Database.DatabaseOperations;

public class GetReceitasTask extends AsyncTask<String, String, String> {
    private String baseUrlreceitas = "https://demo3677899.mockable.io/receitas";
    private String baseUrlversao = "https://demo3677899.mockable.io/versao";
    private RequestQueue requestQueue; // This is our requests queue to process our HTTP requests.

    private int versaoAPI;
    private int versaoInterna;

    private String PREFS_NAME = "SmartCooking_PrefsName";

    private WeakReference<Context> contextRef;

    private boolean error = false;

    private List<Receita> lista_receitas;
    private List<String> lista_ingredientes;

    private Context context;

    public GetReceitasTask(Context context) {
        contextRef = new WeakReference<>(context);
        lista_receitas = new ArrayList<Receita>();
        lista_ingredientes = new ArrayList<String>();
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Context context = contextRef.get();
        requestQueue = Volley.newRequestQueue(context); // This setups up a new request queue which we will need to make HTTP requests.

        /*super.onPreExecute();
        // display a progress dialog for good user experiance
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();*/
    }

    @Override
    protected String doInBackground(String... params) {
        Context context = contextRef.get();

        try {

            URL githubEndpoint = new URL(baseUrlversao);

            // Create connection
            HttpsURLConnection myConnection =
                    (HttpsURLConnection) githubEndpoint.openConnection();

            if (myConnection.getResponseCode() == 200) {
                // Success
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                jsonReader.beginObject();
                if(jsonReader.nextName().equals("versao")){
                    versaoAPI = Integer.parseInt(jsonReader.nextString());
                }
                jsonReader.endObject();

            } else {
                // Error handling code goes here
                error = true;
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            String result = sharedPreferences.getString("Receitas_Version", "");
            if(result.isEmpty() || Integer.parseInt(result) < versaoAPI){

                githubEndpoint = new URL(baseUrlreceitas);

                // Create connection
                myConnection =
                        (HttpsURLConnection) githubEndpoint.openConnection();

                if (myConnection.getResponseCode() == 200) {
                    // Success
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");
                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    parseJson(jsonReader);

                    SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString("Receitas_Version", String.valueOf(versaoAPI));
                    editor.apply();

                } else {
                    // Error handling code goes here
                    error = true;
                }
            }else{
                // Ja guardou as receitas
                // System.out.println("Nao Penetrou");
            }
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
        }

        return null;
    }

    private void parseJson(JsonReader jsonReader) {
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
                                String ingrediente = jsonReader.nextString();
                                array.add(ingrediente);
                                if(!lista_ingredientes.contains(ingrediente)){
                                    lista_ingredientes.add(ingrediente);
                                }
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

        //DatabaseOperations operacoesDB = new DatabaseOperations(context);
        //operacoesDB.apagarTabelas(DatabaseOperations.TODAS_TABELAS);

        /*for (int i=0;i<lista_receitas.size();i++){
            Receita r = lista_receitas.get(i);
            System.out.println(r);
            //operacoesDB.inserirReceita(r);
        }*/
    }

    @Override
    protected void onPostExecute(String s) {


    }

    public List<Receita> getLista_receitas() {
        return lista_receitas;
    }
}