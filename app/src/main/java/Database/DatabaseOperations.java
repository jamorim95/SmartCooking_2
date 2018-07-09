package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.developer.luisgoncalo.smartcooking.Receita;

import java.util.ArrayList;
import java.util.List;

import Database.Ingredientes.IngredienteBaseHelper;
import Database.Ingredientes.IngredienteCursorWrapper;
import Database.Ingredientes.IngredienteDbScheme;
import Database.Receitas.ReceitaBaseHelper;
import Database.Receitas.ReceitaDbScheme;
import Database.RelacaoReceitaIngredientes.RelacaoReceitaIngredientesBaseHelper;
import Database.RelacaoReceitaIngredientes.RelacaoReceitaIngredientesDbScheme;
import Database.RelacaoReceitaPreparacao.RelacaoReceitaPreparacaoBaseHelper;
import Database.RelacaoReceitaPreparacao.RelacaoReceitaPreparacaoDbScheme;

public class DatabaseOperations {

    public static final int TODAS_TABELAS = 0;
    public static final int TABELA_RECEITAS = 1;
    public static final int TABELA_INGREDIENTES = 2;
    public static final int TABELA_RELACAO_RECEITA_INGREDIENTES = 3;
    public static final int TABELA_RELACAO_RECEITA_PREPARACAO = 4;

    private SQLiteDatabase mDatabase_receitas;
    private SQLiteDatabase mDatabase_ingredientes;
    private SQLiteDatabase mDatabase_relacaoReceitaIngredientes;
    private SQLiteDatabase mDatabase_relacaoReceitaPreparacao;

    public DatabaseOperations(Context context){
        mDatabase_receitas = new ReceitaBaseHelper(context).getWritableDatabase();
        mDatabase_ingredientes = new IngredienteBaseHelper(context).getWritableDatabase();
        mDatabase_relacaoReceitaIngredientes = new RelacaoReceitaIngredientesBaseHelper(context).getWritableDatabase();
        mDatabase_relacaoReceitaPreparacao = new RelacaoReceitaPreparacaoBaseHelper(context).getWritableDatabase();
    }


    /*************************************/
    /************   INSERIR   ************/
    /*************************************/


    public void inserirReceita(Receita receita){

        long id_receita = receita.getId();
        List<String> lista_prep = receita.getPreparacao();
        List<String> lista_ingrs_nomes = receita.getIngredientes_simples();

        // fazer o mapeamento dos nomes dos ingredientes para os respectivos ID's
        List<Integer> lista_ingrs_ID = getListaIngredientesID(lista_ingrs_nomes);

        List<String> lista_ingrs_quantidades = receita.getIngredientes();

        // inserir na tabela "Receita"
        ContentValues values_receita = getContentValues_receita(receita);
        mDatabase_receitas.insert(ReceitaDbScheme.ReceitaTable.NAME, null, values_receita);

        // inserir passos de preparação da receita na tabela "Relação Receita-Preparação"
        for(int i=0; i<lista_prep.size(); i++){
            inserirPassoPreparacao(id_receita, i+1, lista_prep.get(i));
        }

        // inserir ingredientes e respectivas quantidades na tabela "Relaçãi Receita-Ingredientes"
        for(int i=0; i<lista_ingrs_ID.size(); i++){
            String quantidade = lista_ingrs_quantidades.get(i);
            inserirRelacaoReceitaIngrediente(id_receita, lista_ingrs_ID.get(i), quantidade);
        }
    }

    private ContentValues getContentValues_receita(Receita r){
        ContentValues values = new ContentValues();
        values.put(ReceitaDbScheme.ReceitaTable.Cols.ID, r.getId());
        values.put(ReceitaDbScheme.ReceitaTable.Cols.NOME, r.getNome());
        values.put(ReceitaDbScheme.ReceitaTable.Cols.DIFICULDADE, r.getDificuldade());
        values.put(ReceitaDbScheme.ReceitaTable.Cols.TEMPO_PREPARACAO, r.getTempo());
        values.put(ReceitaDbScheme.ReceitaTable.Cols.CATEGORIA, r.getCategoria());
        values.put(ReceitaDbScheme.ReceitaTable.Cols.FORNECEDOR, r.getFornecedor());
        values.put(ReceitaDbScheme.ReceitaTable.Cols.IMAGEM, r.getImagem());
        return values;
    }

    private void inserirPassoPreparacao(long id_receita, int num_passo, String passo){
        ContentValues values = getContentValues_relacaoReceitaPreparacao(id_receita, num_passo, passo);
        mDatabase_relacaoReceitaPreparacao.insert(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.NAME, null, values);
    }

    private ContentValues getContentValues_relacaoReceitaPreparacao(long id_receita, int num_passo, String passo){
        ContentValues values = new ContentValues();
        values.put(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.ID_RECEITA, id_receita);
        values.put(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.NUMERO_PASSO, num_passo);
        values.put(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.PASSO, passo);
        return values;
    }

    private void inserirRelacaoReceitaIngrediente(long id_receita, int id_ingr, String quantidade){
        ContentValues values = getContentValues_relacaoReceitaIngrediente(id_receita, id_ingr, quantidade);
        mDatabase_relacaoReceitaIngredientes.insert(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.NAME, null, values);
    }

    private ContentValues getContentValues_relacaoReceitaIngrediente(long id_receita, int id_ingr, String quantidade){
        ContentValues values = new ContentValues();
        values.put(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.ID_RECEITA, id_receita);
        values.put(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.ID_INGREDIENTE, id_ingr);
        values.put(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.QTD_INGREDIENTE, quantidade);
        return values;
    }


    /************************************/
    /************   APAGAR   ************/
    /************************************/


    public boolean apagarTabelas(int tabela){
        // retorna 'True' se o argumento estiver correcto e 'False' caso contrário
        boolean res = true;
        switch(tabela){
            case TODAS_TABELAS:
                mDatabase_receitas.delete(ReceitaDbScheme.ReceitaTable.NAME, null, null);
                mDatabase_ingredientes.delete(IngredienteDbScheme.IngredienteTable.NAME, null, null);
                mDatabase_relacaoReceitaIngredientes.delete(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.NAME, null, null);
                mDatabase_relacaoReceitaPreparacao.delete(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.NAME, null, null);
                break;
            case TABELA_RECEITAS:
                mDatabase_receitas.delete(ReceitaDbScheme.ReceitaTable.NAME, null, null);
                break;
            case TABELA_INGREDIENTES:
                mDatabase_ingredientes.delete(IngredienteDbScheme.IngredienteTable.NAME, null, null);
                break;
            case TABELA_RELACAO_RECEITA_INGREDIENTES:
                mDatabase_relacaoReceitaIngredientes.delete(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.NAME, null, null);
                break;
            case TABELA_RELACAO_RECEITA_PREPARACAO:
                mDatabase_relacaoReceitaPreparacao.delete(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.NAME, null, null);
                break;
            default:
                //TODO: gerar erro
                res = false;
                break;
        }
        return res;
    }

    public void apagarReceita(int id_receita){
        mDatabase_relacaoReceitaIngredientes.delete(RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.NAME, RelacaoReceitaIngredientesDbScheme.RelacaoReceitaIngredientesTable.Cols.ID_RECEITA + " = ?", new String[]{String.valueOf(id_receita)});
        mDatabase_relacaoReceitaPreparacao.delete(RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.NAME, RelacaoReceitaPreparacaoDbScheme.RelacaoReceitaPreparacaoTable.Cols.ID_RECEITA + " = ?", new String[]{String.valueOf(id_receita)});
        mDatabase_receitas.delete(ReceitaDbScheme.ReceitaTable.NAME, ReceitaDbScheme.ReceitaTable.Cols.ID + " = ?", new String[]{String.valueOf(id_receita)});
    }


    /*************************************/
    /************   LEITURA   ************/
    /*************************************/


    private List<Integer> getListaIngredientesID(List<String> lista_nomes_ingredientes){

        // faz o mapeamento duma lista de nomes de ingredientes para os respectivos ID's

        List<Integer> lista_IDs = new ArrayList<Integer>();

        String query_whereClause = IngredienteDbScheme.IngredienteTable.Cols.NOME + " = ?";

        for(String ingrediente : lista_nomes_ingredientes){
            Cursor c = mDatabase_ingredientes.query(IngredienteDbScheme.IngredienteTable.NAME, null, query_whereClause, new String[]{ingrediente}, null, null, null);
            IngredienteCursorWrapper cursor = new IngredienteCursorWrapper(c);

            cursor.moveToFirst();
            lista_IDs.add(cursor.getIngrediente_ID());

        }

        return lista_IDs;
    }


    /***********************************/
    /************   CLOSE   ************/
    /***********************************/


    public void closeAllDatabases(){
        mDatabase_receitas.close();
        mDatabase_ingredientes.close();
        mDatabase_relacaoReceitaIngredientes.close();
        mDatabase_relacaoReceitaPreparacao.close();
    }
}
