package com.developer.luisgoncalo.smartcooking;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AcercaActivity extends AppCompatActivity {

    // Barra Lateral
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    // Textos
    private TextView text_parceiros1_link;
    private TextView text_parceiros2_link;
    private TextView text_parceiros3_link;
    private TextView text_facebook_link;
    private TextView text_twitter_link;
    private TextView text_icons_link;

    // Hiperligações
    private Spanned link_Facebook;
    private Spanned link_Twitter;
    private Spanned link_MaisUmCulinaria;
    private Spanned link_HojePraJantar;
    private Spanned link_UC;
    private Spanned link_Icons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca);

        mDrawerList = findViewById(R.id.navList);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        text_parceiros1_link = findViewById(R.id.parceiros1_txt);
        text_parceiros2_link = findViewById(R.id.parceiros2_txt);
        text_parceiros3_link = findViewById(R.id.parceiros3_txt);
        text_facebook_link = findViewById(R.id.fb_link);
        text_twitter_link = findViewById(R.id.twitter_link);
        text_icons_link = findViewById(R.id.icons_link);


        link_Facebook= fromHtml("<a href=\'https://www.facebook.com/SmartCookingApp/'>SmartCookingApp</a>");
        link_MaisUmCulinaria=fromHtml("<a href=\'http://maisumsobreculinaria.blogspot.pt/'>Mais Um Sobre Culinária</a>");
        link_HojePraJantar=fromHtml("<a href=\'http://hojeparajantar.blogspot.pt/'>Hoje para jantar</a>");
        link_UC=fromHtml("<a href=\'http://uc.pt/'>Universidade de Coimbra</a>");
        link_Twitter=fromHtml("<a href=\'https://twitter.com/smartcookingapp'>@smartcookingapp</a>");
        link_Icons=fromHtml("<a href='https://www.flaticon.com/authors/madebyoliver'>Madebyoliver</a>");


        text_parceiros1_link.setText(link_HojePraJantar);
        text_parceiros1_link.setMovementMethod(LinkMovementMethod.getInstance());

        text_parceiros2_link.setText(link_MaisUmCulinaria);
        text_parceiros2_link.setMovementMethod(LinkMovementMethod.getInstance());

        text_parceiros3_link.setText(link_UC);
        text_parceiros3_link.setMovementMethod(LinkMovementMethod.getInstance());

        text_facebook_link.setText(link_Facebook);
        text_facebook_link.setMovementMethod(LinkMovementMethod.getInstance());

        text_twitter_link.setText(link_Twitter);
        text_twitter_link.setMovementMethod(LinkMovementMethod.getInstance());

        text_icons_link.setText(link_Icons);
        text_icons_link.setMovementMethod(LinkMovementMethod.getInstance());
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
                        i=new Intent(AcercaActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 1:
                        i=new Intent(AcercaActivity.this, PesquisaActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 2:
                        i=new Intent(AcercaActivity.this, CategoriasActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 3:
                        i=new Intent(AcercaActivity.this, TodasActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case 4:
                        mDrawerLayout.closeDrawers();
                        break;
                }

            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            // Called when a drawer has settled in a completely closed state.
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

        // Activate the navigation drawer toggle
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);

    }


    public static Spanned fromHtml(String html){
        Spanned result = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        }
        return result;
    }
}
