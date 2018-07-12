package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.developer.luisgoncalo.smartcooking.Receita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

    public static final int CRESCENTE = 1;
    public static final int DECRESCENTE = -1;


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
        List<String> lista_ingrs_quantidades = receita.getIngredientes();

        //System.out.println(receita);

        inserirIngredientes(lista_ingrs_nomes);

        // fazer o mapeamento dos nomes dos ingredientes para os respectivos ID's
        List<Integer> lista_ingrs_ID = getListaIngredientesID(lista_ingrs_nomes);


        // inserir na tabela "Receita"
        ContentValues values_receita = getContentValues_receita(receita);
        mDatabase_receitas.insert(ReceitaDbScheme.ReceitaTable.NAME, null, values_receita);

        // inserir passos de preparação da receita na tabela "Relação Receita-Preparação"
        for(int i=0; i<lista_prep.size(); i++){
            inserirPassoPreparacao(id_receita, i+1, lista_prep.get(i));
        }

        System.out.println(">>>>>>>>>>>>  receita:  " + receita.getIngredientes());
        System.out.println(">>>>>>>>>>>>  receita:  " + receita.getIngredientes_simples());
        System.out.println(">>>>>>>>> lista_ingrs_ID:  " + lista_ingrs_ID.size());
        System.out.println(">>>>>>>>> lista_ingrs_quantidades:  " + lista_ingrs_quantidades.size());

        //TODO: isto em principio está a funcionar se não houver ingredientes repetidos

        // inserir ingredientes e respectivas quantidades na tabela "Relaçãi Receita-Ingredientes"
        for(int i=0; i<lista_ingrs_ID.size(); i++){
            String nome_ingr = lista_ingrs_nomes.get(i);
            String quantidade = "";//lista_ingrs_quantidades.get(i);
            for(String qtd : lista_ingrs_quantidades){
                if(qtd.toLowerCase(Locale.getDefault()).contains(nome_ingr.toLowerCase(Locale.getDefault()))){
                    quantidade=qtd;
                    break;
                }
            }
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

    private void inserirIngredientes(List<String> nomes){
        ContentValues values;
        for(String nome : nomes){
            if(!existeIngrediente(nome)) {
                values = getContentValues_ingrediente(nome);
                mDatabase_ingredientes.insert(IngredienteDbScheme.IngredienteTable.NAME, null, values);
            }
        }
    }

    private ContentValues getContentValues_ingrediente(String nome){
        ContentValues values = new ContentValues();
        values.put(IngredienteDbScheme.IngredienteTable.Cols.NOME, nome);
        return values;
    }

    private boolean existeIngrediente(String nome){
        String query_whereClause = "LOWER(" + IngredienteDbScheme.IngredienteTable.Cols.NOME + ") = ?";
        Cursor c = mDatabase_ingredientes.query(IngredienteDbScheme.IngredienteTable.NAME, null, query_whereClause, new String[]{nome.toLowerCase(Locale.getDefault())}, null, null, null);
        IngredienteCursorWrapper cursor = new IngredienteCursorWrapper(c);

        return cursor.moveToFirst();
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
            System.out.println(">>>>>>>>>>>> ingr: " + ingrediente);
            Cursor c = mDatabase_ingredientes.query(IngredienteDbScheme.IngredienteTable.NAME, null, query_whereClause, new String[]{ingrediente}, null, null, null);
            IngredienteCursorWrapper cursor = new IngredienteCursorWrapper(c);

            cursor.moveToFirst();
            int id = cursor.getIngrediente_ID();
            if(!lista_IDs.contains(id)){
                lista_IDs.add(id);
            }

        }
        System.out.println(">>>>>>> lista_IDs size:  " + lista_IDs.size());
        return lista_IDs;
    }

    public List<String> getListaNomeIngredientes(){
        List<String> lista_ingrs = new ArrayList<String>();

        Cursor c = mDatabase_ingredientes.query(IngredienteDbScheme.IngredienteTable.NAME, null, null, null, null, null, null);
        IngredienteCursorWrapper cursor = new IngredienteCursorWrapper(c);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            lista_ingrs.add(cursor.getIngrediente()[1]);
            cursor.moveToNext();
        }

        return lista_ingrs;
    }


    /***********************************/
    /************   CLOSE   ************/
    /***********************************/


    public boolean closeAllDatabases(int tabela){
        boolean res = true;
        switch(tabela){
            case TODAS_TABELAS:
                mDatabase_receitas.close();
                mDatabase_ingredientes.close();
                mDatabase_relacaoReceitaIngredientes.close();
                mDatabase_relacaoReceitaPreparacao.close();
                break;
            case TABELA_RECEITAS:
                mDatabase_receitas.close();
                break;
            case TABELA_INGREDIENTES:
                mDatabase_ingredientes.close();
                break;
            case TABELA_RELACAO_RECEITA_INGREDIENTES:
                mDatabase_relacaoReceitaIngredientes.close();
                break;
            case TABELA_RELACAO_RECEITA_PREPARACAO:
                mDatabase_relacaoReceitaPreparacao.close();
                break;
            default:
                //TODO: gerar erro
                res = false;
                break;
        }

        return res;
    }


    /***************************************************/
    /************   LEITURA EM ARRAY_LIST   ************/
    /***************************************************/

    /*
    *  @param listaGeral arrayList com todas as receitas da base de dados
    *  @param ingredientesPesquisados arrayList com os ingredientes pesquisados
    *  @return arrayList com as receitas encontradas
    *
    * */
    public ArrayList<Receita> procurarPorIngredientes(List<Receita> listaGeral, ArrayList<String> ingredientesPesquisados){

        ArrayList<Receita> lista = new ArrayList<Receita>();
        HashMap<Double, ArrayList<Receita>> mapa = new HashMap<Double, ArrayList<Receita>>();

        int num_match;          // nº de ingredientes da receita COM match na pesquisa
        int num_not_match;      // nº de ingredientes da receita SEM match na pesquisa
        int num_TOTAL_ingrs;    // nº TOTAL de ingredientes da receita

        double coeficiente_ordenacao;  // (num_match - num_NOT_match) / num_TOTAL_ingrs
        ArrayList<Double> lista_coefecientes_ordenacao = new ArrayList<Double>();

        ArrayList<Receita> aux_listaReceitas;

        for(Receita r : listaGeral){
            num_match=0;
            num_not_match=0;
            num_TOTAL_ingrs=r.getIngredientes_simples().size();

            for(String ingr : r.getIngredientes_simples()){
                if(ingredientesPesquisados.contains(ingr)){
                    num_match++;
                }else{
                    num_not_match++;
                }
            }

            if(num_match > 0){
                // se esta receita tiver pelo menos 1 ingrediente com match na pesquisa

                coeficiente_ordenacao = (num_match - num_not_match) / num_TOTAL_ingrs;

                if(mapa.containsKey(coeficiente_ordenacao)){
                    aux_listaReceitas=mapa.get(coeficiente_ordenacao);
                }else{
                    aux_listaReceitas = new ArrayList<Receita>();
                    lista_coefecientes_ordenacao.add(coeficiente_ordenacao);
                }
                aux_listaReceitas.add(r);
                mapa.put(coeficiente_ordenacao, aux_listaReceitas);

            }
        }

        if(!lista_coefecientes_ordenacao.isEmpty()){
            // se for encontrada pelo menos 1 receita com pelo menos 1 ingrediente com match na pesquisa

            //TODO: testar este ordenamento dos coeficientes de ordenamento das receitas
            double[] aux_array = getDoubleArray(lista_coefecientes_ordenacao);
            Arrays.sort(aux_array);     // isto faz um Dual-Pivot Quicksort (avg: O(log(n))   worst: O(n^2)  )

            System.out.println(">>>>>>>>>>>>>>> LISTA DE COEFICIENTES DE ORDENAMENTO DAS RECEITAS:");
            for(double coef : aux_array){
                System.out.println(">>>>>>>>>>>>>>>  " + coef);
                aux_listaReceitas = mapa.get(coef);

                //TODO:  testar este ordenamento
                Collections.sort(aux_listaReceitas, new Comparator<Receita>() {
                    @Override
                    public int compare(Receita r1, Receita r2) {
                        return r1.getIngredientes_simples().size() - r2.getIngredientes_simples().size();   // para ordem crescente
                    }
                });
                mapa.put(coef, aux_listaReceitas);

                lista.addAll(aux_listaReceitas);    // adidiona as receitas já ordenadas à lista que vai ser retornada
            }
        }

        for(Receita r : lista){
            System.out.println(r);
        }

        return lista;
    }

    private double[] getDoubleArray(ArrayList<Double> lista){
        double[] target = new double[lista.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = lista.get(i);
        }
        return target;
    }

    /*
     *  @param listaGeral arrayList com todas as receitas da base de dados
     *  @param categoria string com a categoria pesquisada
     *  @return arrayList com as receitas encontradas
     *
     * */
    public ArrayList<Receita> procurarPorCategoria(List<Receita> listaGeral, String categoria){
        ArrayList<Receita> lista = new ArrayList<Receita>();

        for(Receita r : listaGeral){
            if(r.getCategoria().toLowerCase().equals(categoria.toLowerCase())){
                lista.add(r);
            }
        }

        return lista;
    }

    /*
     *  @param listaGeral arrayList com todas as receitas da base de dados
     *  @return arrayList com todos os ingredientes que existem na ArrayList das SharedPreferences
     *
     * */
    public String[] getListaNomeIngredientes(List<Receita> listaGeral){
        ArrayList<String> lista = new ArrayList<String>();
        for(Receita r : listaGeral){
            for(String ingr : r.getIngredientes_simples()){
                if(lista.contains(ingr)){
                    continue;
                }
                lista.add(ingr);
            }
        }
        String[] aux=getStringArray(lista);
        Arrays.sort(aux);
        return appendFirstElement(aux);
    }

    private String[] getStringArray(ArrayList<String> lista){
        String[] target = new String[lista.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = lista.get(i);
        }
        return target;
    }

    private String[] appendFirstElement(String[] array){
        String[] aux = new String[array.length+1];
        aux[0] = "Selecione um ingrediente";
        for(int i=0; i<array.length; i++){
            aux[i+1] = array[i];
        }
        return aux;
    }


}
