package Database;

public class RelacaoReceitaIngredientes {
    public static final class  RelacaoReceitaIngredientesTable{
        public static final String NAME = " RelacaoReceitaIngredientes";

        public static final class Cols{
            public static final String ID_RECEITA = "id_receita";
            public static final String ID_INGREDIENTE = "id_ingrediente";
            public static final String QTD_INGREDIENTE = "quantidade";
        }
    }
}
