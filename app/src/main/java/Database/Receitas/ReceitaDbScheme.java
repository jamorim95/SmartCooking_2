package Database.Receitas;

public class ReceitaDbScheme {
    public static final class ReceitaTable{
        public static final String NAME = "Receitas";

        public static final class Cols{
            public static final String ID = "id";
            public static final String NOME = "nome";
            public static final String DIFICULDADE = "dificuldade";
            public static final String TEMPO_PREPARACAO = "tempo";
            public static final String CATEGORIA = "categoria";
            public static final String FORNECEDOR = "fornecedor";
            public static final String IMAGEM = "imagem";
        }
    }
}
