package Database.Ingredientes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IngredienteBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "smartcookingDatabase_ingredientes.db";

    public IngredienteBaseHelper (Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + IngredienteDbScheme.IngredienteTable.NAME + " ( " +
                IngredienteDbScheme.IngredienteTable.Cols.ID + " integer primary key autoincrement, " +
                IngredienteDbScheme.IngredienteTable.Cols.NOME + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
