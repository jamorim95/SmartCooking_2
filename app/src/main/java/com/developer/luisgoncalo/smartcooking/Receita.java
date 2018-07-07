package com.developer.luisgoncalo.smartcooking;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class Receita implements Serializable,Comparable<Receita> {
    private long id;
    private String nome;
    private int dificuldade;
    private int tempo; //Em minutos
    private int n_ingredientes;
    private String categoria;
    private String fornecedor;
    private List<String> preparacao;
    private List<String> ingredientes;
    private String imagem;
    private List<String> ingredientes_simples;


    public Receita(){

    }

    public Receita(int id, String nome, int tempo, int dificuldade, String tipo) {
        this.id = id;
        this.nome = nome;
        this.tempo = tempo;
        this.dificuldade = dificuldade;
        this.categoria = tipo;
    }

    public Receita(int id, String nome, int tempo, int dificuldade, String tipo, String url) {
        this.id = id;
        this.nome = nome;
        this.tempo = tempo;
        this.dificuldade = dificuldade;
        this.categoria = tipo;
        this.imagem = url;
    }

    public Receita(long id, String nome, int tempo, int dificuldade, int n_ingredientes, String categoria, String fornecedor, List<String> preparacao, List<String> ingredientes, String imagem, List<String> ingredientes_simples) {
        this.id = id;
        this.nome = nome;
        this.dificuldade = dificuldade;
        this.tempo = tempo;
        this.n_ingredientes = n_ingredientes;
        this.categoria = categoria;
        this.fornecedor = fornecedor;
        this.preparacao = preparacao;
        this.ingredientes = ingredientes;
        this.imagem = imagem;
        this.ingredientes_simples = ingredientes_simples;
    }

    public Receita(int id, String nome, int tempo, int dificuldade, String categoria, List<String> preparacao, String imagem) {
        this.id = id;
        this.nome = nome;
        this.dificuldade = dificuldade;
        this.tempo = tempo;
        this.categoria = categoria;
        this.preparacao = preparacao;
        this.imagem = imagem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(int dificuldade) {
        this.dificuldade = dificuldade;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getN_ingredientes() {
        return n_ingredientes;
    }

    public void setN_ingredientes(int n_ingredientes) {
        this.n_ingredientes = n_ingredientes;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<String> getPreparacao() {
        return preparacao;
    }

    public void setPreparacao(List<String> preparacao) {
        this.preparacao = preparacao;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public List<String> getIngredientes_simples() {
        return ingredientes_simples;
    }

    public void setIngredientes_simples(List<String> ingredientes_simples) {
        this.ingredientes_simples = ingredientes_simples;
    }

    public String getPreparacaoString(){
        StringBuilder prep = new StringBuilder();

        for (String s : this.preparacao)
        {
            prep.append("- ").append(s).append("\n");
        }

        return prep.toString();
    }

    @Override
    public String toString() {
        return "Receita{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dificuldade=" + dificuldade +
                ", tempo=" + tempo +
                ", n_ingredientes=" + n_ingredientes +
                ", categoria='" + categoria + '\'' +
                ", fornecedor='" + fornecedor + '\'' +
                ", preparacao=" + preparacao +
                ", ingredientes=" + ingredientes +
                ", imagem='" + imagem + '\'' +
                ", ingredientes_simples=" + ingredientes_simples +
                '}';
    }

    static Comparator<Receita> ReceitaNameComparator = new Comparator<Receita>() {

        public int compare(Receita left, Receita right) {

            String leftName = left.getNome().toUpperCase();
            String rightName = right.getNome().toUpperCase();

            //ascending order
            return leftName.compareTo(rightName);

            //descending order
            //return fruitName2.compareTo(fruitName1);

            //Weight compare
            //return left.getPesoPesquisa() - right.getPesoPesquisa();
        }

    };

    @Override
    public int compareTo(@NonNull Receita receita) {
        return 0;
    }
}
