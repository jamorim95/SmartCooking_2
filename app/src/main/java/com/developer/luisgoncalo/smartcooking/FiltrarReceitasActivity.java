package com.developer.luisgoncalo.smartcooking;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiltrarReceitasActivity extends AppCompatActivity {
    private String categoria;
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

        categoria = getIntent().getStringExtra("categoria");

        // Adding items to listview
        List<Receita> receitas = criarReceitas(); //Get receitas por categoria
        adapter = new MyAdapter(receitas, this,FiltrarReceitasActivity.this);
        lista_receitas.setAdapter(adapter);
    }

    private List<Receita> criarReceitas() {
        return new ArrayList<>(Arrays.asList(
            new Receita(1,"Tostas de Frango no Forno",15,3,"Snack", Arrays.asList("Levar um tacho ao lume com a cebola picada e uma colher de sopa de azeite e deixar refogar","Acrescentar o frango desfiado e envolver bem.","Polvilhar com a farinha e junte o leite.","Deixar cozinhar, mexendo sempre de modo a ficar com uma espécie de creme.","Temperar com sal, pimenta e reserve.","Rechear duas fatias de pão com o creme de frango e cubra com o queijo.","Tapar com a outra fatia de pão e leve à tostadeira para tostar o pão e derreter o queijo.","Servir ainda quente."),"https://smartcookingapp.files.wordpress.com/2015/10/receita_pgi.jpg"),
            new Receita(2,"Salsichas com Queijo e Fiambre",15,3,"Carne", Arrays.asList("Estender uma fatia de fiambre e, por cima colocar uma fatia de queijo.|Barrar o queijo com a mostarda a seu gosto.","Colocar a salsicha por cima e enrolar nas fatias.","Polvilhar as salsichas enroladas com o queijo mozzarella.","Colocar no forno e deixe ficar até gratinar o queijo e está pronto a servir!"),"https://smartcookingapp.files.wordpress.com/2015/10/sals.jpg")
        ));
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
