package Database.Receitas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Database.Ingredientes.IngredienteDbScheme;

public class ReceitaBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "smartcookingDatabase_receitas.db";

    public ReceitaBaseHelper (Context context){
        super(context, DATABASE_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ReceitaDbScheme.ReceitaTable.NAME + " ( " +
                ReceitaDbScheme.ReceitaTable.Cols.ID + " integer primary key autoincrement, " +
                ReceitaDbScheme.ReceitaTable.Cols.NOME + ", " +
                ReceitaDbScheme.ReceitaTable.Cols.DIFICULDADE + ", " +
                ReceitaDbScheme.ReceitaTable.Cols.TEMPO_PREPARACAO + ", " +
                ReceitaDbScheme.ReceitaTable.Cols.CATEGORIA + ", " +
                ReceitaDbScheme.ReceitaTable.Cols.FORNECEDOR + ", " +
                ReceitaDbScheme.ReceitaTable.Cols.IMAGEM + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
