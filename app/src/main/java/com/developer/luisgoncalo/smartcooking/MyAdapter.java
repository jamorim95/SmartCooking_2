package com.developer.luisgoncalo.smartcooking;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by alex on 02/07/17.
 */

class MyAdapter extends BaseAdapter{

    private final List<Receita> receitas;
    private ArrayList<Receita> arraylist;
    private final Activity act;
    private final Context c;

    MyAdapter(List<Receita> receitas, Activity act, Context c) {
        this.receitas = receitas;
        this.act = act;
        this.c = c;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(receitas);
    }

    public class ViewHolder {
        TextView nome;
        TextView dificuldade;
        ImageView imagem;
    }

    @Override
    public int getCount() {
        return receitas.size();
    }

    @Override
    public Object getItem(int position) {
        return receitas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = act.getLayoutInflater().inflate(R.layout.list_item,parent, false);

            // Locate the TextViews in listview_item.xml
            holder.nome = view.findViewById(R.id.receita_name);
            holder.dificuldade = view.findViewById(R.id.receita_difficult);
            holder.imagem = view.findViewById(R.id.receita_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Receita receita = receitas.get(position);

        // Set the results into TextViews
        holder.nome.setText(receitas.get(position).getNome());
        String dificuldade;
        switch (receitas.get(position).getDificuldade()) {
            case 1:
                dificuldade = "Facil";
                break;
            case 2:
                dificuldade = "Intermedio";
                break;

            case 3:
                dificuldade = "Avançado";
                break;

            default:
                dificuldade = "Lendário";
                break;
        }
        holder.dificuldade.setText(dificuldade);

        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(c, DetalhesActivity.class);
                i.putExtra("receita", receitas.get(position));
                c.startActivity(i);
                //Toast.makeText(c, "You clicked Item: " + receitas.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        receitas.clear();

        if (charText.length() == 0) {
            receitas.addAll(arraylist);
        }
        else {
            for (Receita r : arraylist)
            {
                if (r.getNome().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    receitas.add(r);
                }
            }
        }
        notifyDataSetChanged();
    }

}
