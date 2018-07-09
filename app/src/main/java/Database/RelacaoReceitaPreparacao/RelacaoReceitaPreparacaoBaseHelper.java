package Database.RelacaoReceitaPreparacao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RelacaoReceitaPreparacaoBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "smartcookingDatabase_relacaoReceitaPreparacao.db";

    public RelacaoReceitaPreparacaoBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.NAME + " ( " +
                RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.ID_RECEITA + ", " +
                RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.NUMERO_PASSO + ", " +
                RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.PASSO + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
