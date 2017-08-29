package com.android.chat.chat.Chat.Mensagens;


import com.android.chat.chat.Chat.Nomes.Personagem;

public class Mensagens {
    private String room;
    private Personagem cliente;
    private String dia,hora;
    private String msg;
    private int type;

    public Mensagens(String room,Personagem cliente, String dia, String hora, String msg, int typ){
        this.room= room;
        this.cliente= cliente;
        this.dia= dia;
        this.hora =hora;
        this.msg= msg;
        this.type = typ;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Personagem getCliente() {
        return cliente;
    }

    public String getDia() {
        return dia;
    }

    public String getHora() {
        return hora;
    }

    public String getRoom() {
        return room;
    }

    public void setCliente(Personagem cliente) {
        this.cliente = cliente;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
