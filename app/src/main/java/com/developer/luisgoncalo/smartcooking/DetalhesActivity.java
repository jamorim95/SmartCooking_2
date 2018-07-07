package com.developer.luisgoncalo.smartcooking;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Receita receita = (Receita) getIntent().getSerializableExtra("receita");

        //Toast.makeText(DetalhesActivity.this, receita.getNome(), Toast.LENGTH_SHORT).show();

        final Toolbar mToolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setTitle(receita.getNome());

        AppBarLayout mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
            }
        });

        //TextView receita_title = findViewById(R.id.receita_detalhes_titulo);
        //TextView receita_subtitle = findViewById(R.id.receita_detalhes_subtitulo);
        TextView receita_texto = findViewById(R.id.receita_detalhes_preparacao);

        String subtitulo = "Tempo: ";
        subtitulo += receita.getTempo() + " min, Dificuldade: ";

        switch (receita.getDificuldade()) {
            case 1:
                subtitulo += "Facil";
                break;
            case 2:
                subtitulo += "Intermedio";
                break;

            case 3:
                subtitulo += "Avançado";
                break;

            default:
                subtitulo += "Lendário";
                break;
        }

        //receita_title.setText(receita.getNome());
        //receita_subtitle.setText(subtitulo);
        //receita_texto.setText(receita.getPreparacaoString());

        /*ImageView iv = findViewById(R.id.receita_image);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(DetalhesActivity.this));
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(receita.getImagem(), iv);*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}