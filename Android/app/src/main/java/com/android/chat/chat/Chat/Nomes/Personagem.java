package com.android.chat.chat.Chat.Nomes;


public class Personagem {
    private String _id;
    private String nome;
    private int imagem;

    public Personagem(String nome, int imagem){
        this.nome= nome;
        this.imagem= imagem;
    }

    public Personagem(String nome){
        this.nome= nome;
    }

    public Personagem(){

    }

    public int getImagem() {
        return imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
