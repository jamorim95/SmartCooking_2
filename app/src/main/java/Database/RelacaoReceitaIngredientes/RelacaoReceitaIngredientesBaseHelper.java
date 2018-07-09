package Database.RelacaoReceitaIngredientes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Database.Ingredientes.IngredienteDbScheme;

public class RelacaoReceitaIngredientesBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "smartcookingDatabase_relacaoReceitaIngredientes.db";

    public RelacaoReceitaIngredientesBaseHelper (Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.NAME + " ( " +
                RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.ID_RECEITA + ", " +
                RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.ID_INGREDIENTE + ", " +
                RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.QTD_INGREDIENTE + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
