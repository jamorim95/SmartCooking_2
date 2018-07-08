package Database.RelacaoReceitaPreparacao;

public class RelacaoReceitaPreparacaoDbScheme {
    public static final class  RelacaoReceitaPreparacaoTable{
        public static final String NAME = " RelacaoReceitaPreparacaoDbScheme";

        public static final class Cols{
            public static final String ID_RECEITA = "id_receita";
            public static final String NUMERO_PASSO = "numero_passo"; //para indicar a ordem dos passos de preparação de cada receita
            public static final String PASSO = "passo";
        }
    }
}
