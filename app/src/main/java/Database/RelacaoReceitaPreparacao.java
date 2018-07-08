package Database;

public class RelacaoReceitaPreparacao {
    public static final class  RelacaoReceitaPreparacaoTable{
        public static final String NAME = " RelacaoReceitaPreparacao";

        public static final class Cols{
            public static final String ID_RECEITA = "id_receita";
            public static final String NUMERO_PASSO = "numero_passo"; //para indicar a ordem dos passos de preparação de cada receita
            public static final String PASSO = "passo";
        }
    }
}
