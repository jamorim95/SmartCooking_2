package com.developer.luisgoncalo.smartcooking;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import utils.ObjectSerializer;

public class MainActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private String PREFS_NAME = "SmartCooking_PrefsName";
    private String PREFS_LISTA_RECEITAS = "SmartCooking_lista_receitas";

    private ArrayList<Receita> lista_receitas;

    private Button maisIngredientes;
    private Button menosIngredientes;
    private int nIngredientesPesquisa;

    private Spinner[] listas_ingredientes;
    private LinearLayout layout_spinners;

    private Button search_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_button = (Button) findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> ingrs = new ArrayList<String>();
                boolean flag=true;
                for(int i=0; i<nIngredientesPesquisa; i++){
                    Spinner sp = listas_ingredientes[i];
                    if(sp.getSelectedItemPosition()==0){
                        Toast.makeText(MainActivity.this, "Tem que selecionar todos os ingredientes ou diminuir o tamanho da pesquisa.", Toast.LENGTH_LONG).show();
                        flag=false;
                        break;
                    }else{
                        ingrs.add((String) sp.getSelectedItem());
                    }
                }
                if(flag){
                    Intent i=new Intent(MainActivity.this, IngredientesActivity.class);
                    i.putExtra("lista_ingrs", ingrs);
                    startActivity(i);
                }
            }
        });

        listas_ingredientes = new Spinner[5];
        listas_ingredientes[0]=(Spinner) findViewById(R.id.ingr_spinner1);
        listas_ingredientes[1]=(Spinner) findViewById(R.id.ingr_spinner2);
        listas_ingredientes[2]=(Spinner) findViewById(R.id.ingr_spinner3);
        listas_ingredientes[3]=(Spinner) findViewById(R.id.ingr_spinner4);
        listas_ingredientes[4]=(Spinner) findViewById(R.id.ingr_spinner5);

        layout_spinners = (LinearLayout) findViewById(R.id.layout_spinners);


        nIngredientesPesquisa=5;

        mDrawerList = findViewById(R.id.navList);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        maisIngredientes = (Button) findViewById(R.id.more_ingr);
        menosIngredientes = (Button) findViewById(R.id.less_ingr);
        maisIngredientes.setEnabled(false);

        maisIngredientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nIngredientesPesquisa<5){
                    nIngredientesPesquisa++;
                    Toast.makeText(MainActivity.this, "CHEGOU AO LISTENER MAIS:  " + (nIngredientesPesquisa-1), Toast.LENGTH_LONG).show();
                    layout_spinners.addView((View)listas_ingredientes[nIngredientesPesquisa-1].getParent());
                    if(nIngredientesPesquisa==5){
                        maisIngredientes.setEnabled(false);
                    }
                    menosIngredientes.setEnabled(true);
                }
            }
        });

        menosIngredientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nIngredientesPesquisa>1){
                    layout_spinners.removeView((View)listas_ingredientes[nIngredientesPesquisa-1].getParent());
                    Toast.makeText(MainActivity.this, "CHEGOU AO LISTENER MENOS:  " + (nIngredientesPesquisa-1), Toast.LENGTH_LONG).show();
                    nIngredientesPesquisa--;

                    maisIngredientes.setEnabled(true);
                    if(nIngredientesPesquisa==1){
                        menosIngredientes.setEnabled(false);
                    }
                }
            }
        });

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        try {
            lista_receitas = (ArrayList<Receita>) ObjectSerializer.deserialize(sharedPreferences.getString(PREFS_LISTA_RECEITAS, ObjectSerializer.serialize((Serializable) new ArrayList<Receita>())));
            Toast.makeText(MainActivity.this, lista_receitas.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addDrawerItems() {
        String osArray[] = getResources().getStringArray(R.array.menu);

        mAdapter = new ArrayAdapter<>(this, R.layout.menu, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                switch (position){
                    case 0:
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        i=new Intent(MainActivity.this, PesquisaActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 2:
                        i=new Intent(MainActivity.this, CategoriasActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 3:
                        i=new Intent(MainActivity.this, TodasActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 4:
                        i=new Intent(MainActivity.this, AcercaActivity.class);
                        startActivity(i);
                        finish();
                        break;
                }

            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }
}