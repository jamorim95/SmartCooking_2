package Database.RelacaoReceitaIngredientes;

import android.database.Cursor;
import android.database.CursorWrapper;


public class RelacaoReceitaIngredientesCursorWrapper extends CursorWrapper {
    public RelacaoReceitaIngredientesCursorWrapper(Cursor cursor) { super(cursor);}

    public String[] getRelacaoReceitaIngredientes(){
        String[] relacao = new String[3];

        String ID_receita = getString(getColumnIndex(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.ID_RECEITA));
        String ID_ingrediente = getString(getColumnIndex(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.ID_INGREDIENTE));
        String quantidade = getString(getColumnIndex(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.QTD_INGREDIENTE));

        relacao[0]=ID_receita;
        relacao[1]=ID_ingrediente;
        relacao[3]=quantidade;

        return relacao;
    }
}
