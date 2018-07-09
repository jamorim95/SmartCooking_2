package Database.Receitas;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.developer.luisgoncalo.smartcooking.Receita;

import Database.Ingredientes.IngredienteDbScheme;

public class ReceitaCursorWrapper extends CursorWrapper {
    public ReceitaCursorWrapper(Cursor cursor) { super(cursor);}

    public Receita getReceita(){
        Receita r = new Receita();

        String ID = getString(getColumnIndex(ReceitaDbScheme.ReceitaTable.Cols.ID));
        String nome = getString(getColumnIndex(ReceitaDbScheme.ReceitaTable.Cols.NOME));
        String dificuldade = getString(getColumnIndex(ReceitaDbScheme.ReceitaTable.Cols.DIFICULDADE));
        String tempo_prep = getString(getColumnIndex(ReceitaDbScheme.ReceitaTable.Cols.TEMPO_PREPARACAO));
        String categoria = getString(getColumnIndex(ReceitaDbScheme.ReceitaTable.Cols.CATEGORIA));
        String fornecedor = getString(getColumnIndex(ReceitaDbScheme.ReceitaTable.Cols.FORNECEDOR));
        String imagem = getString(getColumnIndex(ReceitaDbScheme.ReceitaTable.Cols.IMAGEM));

        r.setId(Long.parseLong(ID));
        r.setNome(nome);
        r.setDificuldade(Integer.parseInt(dificuldade));
        r.setTempo(Integer.parseInt(tempo_prep));
        r.setCategoria(categoria);
        r.setFornecedor(fornecedor);
        r.setImagem(imagem);

        return r;
    }
}
