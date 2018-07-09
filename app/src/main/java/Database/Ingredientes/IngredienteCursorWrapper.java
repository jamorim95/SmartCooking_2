package Database.Ingredientes;

import android.database.Cursor;
import android.database.CursorWrapper;

public class IngredienteCursorWrapper extends CursorWrapper {

    public IngredienteCursorWrapper(Cursor cursor) { super(cursor);}

    public String[] getIngrediente(){
        String[] ingr = new String[2];

        String ID = getString(getColumnIndex(IngredienteDbScheme.IngredienteTable.Cols.ID));
        String nome = getString(getColumnIndex(IngredienteDbScheme.IngredienteTable.Cols.NOME));

        ingr[0]=ID;
        ingr[1]=nome;

        return ingr;
    }

    public String getIngrediente_nome(){

        String nome = getString(getColumnIndex(IngredienteDbScheme.IngredienteTable.Cols.NOME));

        return nome;
    }

    public int getIngrediente_ID(){

        int ID = getInt(getColumnIndex(IngredienteDbScheme.IngredienteTable.Cols.ID));

        return ID;
    }
}
