package Database.RelacaoReceitaPreparacao;

import android.database.Cursor;
import android.database.CursorWrapper;

import Database.RelacaoReceitaIngredientes.RelacaoReceitaIngredientesDbScheme;

public class RelacaoReceitaPreparacaoCursorWrapper extends CursorWrapper {
    public RelacaoReceitaPreparacaoCursorWrapper(Cursor cursor) { super(cursor);}

    public String[] getRelacaoReceitaPreparacao(){
        String[] relacao = new String[3];

        String ID_receita = getString(getColumnIndex(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.ID_RECEITA));
        String num_passo = getString(getColumnIndex(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.NUMERO_PASSO));
        String passo = getString(getColumnIndex(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.PASSO));

        relacao[0]=ID_receita;
        relacao[1]=num_passo;
        relacao[3]=passo;

        return relacao;
    }
}
