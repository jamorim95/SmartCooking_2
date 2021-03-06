package com.developer.luisgoncalo.smartcooking;

import android.content.Intent;
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
import android.widget.ListView;

public class CategoriasActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private Button carne_btn;
    private Button peixe_btn;
    private Button vegan_btn;
    private Button sobremesa_btn;
    private Button snack_btn;
    private Button outros_btn;

    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        mDrawerList = findViewById(R.id.navList);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        carne_btn = findViewById(R.id.carne_btn);
        peixe_btn = findViewById(R.id.peixe_btn);
        vegan_btn = findViewById(R.id.vegan_btn);
        sobremesa_btn = findViewById(R.id.sobremesa_btn);
        snack_btn = findViewById(R.id.snack_btn);
        outros_btn = findViewById(R.id.outro_btn);

        i = new Intent(CategoriasActivity.this, FiltrarReceitasActivity.class);


        carne_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("categoria", "Carne");
                startActivity(i);
                //finish();
            }
        });

        peixe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("categoria", "Peixe");
                startActivity(i);
                //finish();
            }
        });

        vegan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("categoria", "Vegetariano");
                startActivity(i);
                //finish();
            }
        });

        sobremesa_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("categoria", "Sobremesa");
                startActivity(i);
                //finish();
            }
        });

        outros_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("categoria", "Outros");
                startActivity(i);
                //finish();
            }
        });

        snack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("categoria", "Snack");
                startActivity(i);
                //finish();
            }
        });

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
                        i=new Intent(CategoriasActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 1:
                        i=new Intent(CategoriasActivity.this, PesquisaActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 2:
                        mDrawerLayout.closeDrawers();
                        break;
                    case 3:
                        i=new Intent(CategoriasActivity.this, TodasActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 4:
                        i=new Intent(CategoriasActivity.this, AcercaActivity.class);
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
                if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Menu");
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(mActivityTitle);
                }
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

        // Activate the navigation drawer toggle
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }
}