package com.developer.luisgoncalo.smartcooking;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Database.DatabaseOperations;
import utils.ObjectSerializer;

public class IngredientesActivity extends AppCompatActivity {

    private String PREFS_NAME = "SmartCooking_PrefsName";
    private String PREFS_LISTA_RECEITAS = "SmartCooking_lista_receitas";

    private ArrayList<String> lista_ingrs;
    private ArrayList<Receita> receitas;

    // List view
    private ListView lista_receitas;

    // Listview Adapter
    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_receitas);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        lista_receitas = findViewById(R.id.layout_FiltrarReceitas);

        lista_ingrs = (ArrayList<String>) getIntent().getSerializableExtra("lista_ingrs");

        // Adding items to listview
        List<Receita> receitas = criarReceitas(); //Get receitas por categoria
        adapter = new MyAdapter(receitas, this,IngredientesActivity.this);
        lista_receitas.setAdapter(adapter);
    }

    private List<Receita> criarReceitas() {
        List<Receita> receitas;
        List<Receita> res = new ArrayList<Receita>();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        try {
            receitas= (List<Receita>) ObjectSerializer.deserialize(sharedPreferences.getString(PREFS_LISTA_RECEITAS, ObjectSerializer.serialize((Serializable) new ArrayList<Receita>())));

            DatabaseOperations operacoesDB = new DatabaseOperations(this);
            res = operacoesDB.procurarPorIngredientes(receitas, lista_ingrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
