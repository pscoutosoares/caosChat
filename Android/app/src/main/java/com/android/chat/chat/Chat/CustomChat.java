package com.android.chat.chat.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chat.chat.Chat.Mensagens.Mensagens;
import com.android.chat.chat.Chat.Nomes.Personagem;
import com.android.chat.chat.R;

import java.util.ArrayList;

public class CustomChat extends RecyclerView.Adapter<CustomChat.Chat> {


    public class Chat extends RecyclerView.ViewHolder {
        private TextView txReceiv, txSend;
        private ImageView imgReceiv, imgSend;

        public Chat(View itemView) {
            super(itemView);
            txReceiv = (TextView)itemView.findViewById(R.id.msgreceiv);
            txSend = (TextView)itemView.findViewById(R.id.msgsend);
            imgReceiv = (ImageView) itemView.findViewById(R.id.imgreceiv);
            imgSend = (ImageView)itemView.findViewById(R.id.imgsend);
        }
    }
    private ArrayList<Mensagens> mensagens;
    private Context context;

    public CustomChat(Context context, ArrayList<Mensagens> mensagens){
        this.context= context;
        this.mensagens= mensagens;
    }


    @Override
    public CustomChat.Chat onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_custom, parent, false);

        Chat vh = new Chat(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomChat.Chat holder, int position) {

        if(mensagens.get(position).getType()== -1){
            holder.txSend.setVisibility(View.GONE);
            holder.imgSend.setVisibility(View.GONE);
            //holder.txReceiv.setText(mensagens.get(position).getCliente()+": "+ mensagens.get(position).getMsg());
            holder.txReceiv.setText(mensagens.get(position).getMsg());
            holder.imgReceiv.setImageResource(mensagens.get(position).getCliente().getImagem());
        }
        else{
            holder.txReceiv.setVisibility(View.GONE);
            holder.imgReceiv.setVisibility(View.GONE);
            //holder.txSend.setText(mensagens.get(position).getCliente()+": "+ mensagens.get(position).getMsg());
            holder.txSend.setText(mensagens.get(position).getMsg());
            holder.imgSend.setImageResource(mensagens.get(position).getCliente().getImagem());
        }

    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

}
