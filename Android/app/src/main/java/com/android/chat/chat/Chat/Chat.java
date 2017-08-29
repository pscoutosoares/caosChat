package com.android.chat.chat.Chat;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.chat.chat.Chat.Mensagens.Mensagens;
import com.android.chat.chat.Chat.Nomes.Personagem;
import com.android.chat.chat.Chat.Nomes.Personagens;
import com.android.chat.chat.Chat.Socket.Socket;
import com.android.chat.chat.Login.Login;
import com.android.chat.chat.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment implements View.OnClickListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private io.socket.client.Socket servSocket;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Mensagens> mensagens = new ArrayList<>();
    private EditText sendTv;
    private Button sendBt;
    private Personagem CLIENTE_NAME= new Personagem();
    private Personagem CLIENTE_FRIEND = new Personagem();
    private boolean STATUS_SAVE = true;
    private int number_client=0;

    public Chat() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_fragment, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        sendTv= (EditText) v.findViewById(R.id.sendTv);
        sendBt= (Button) v.findViewById(R.id.sendBt);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleview);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        
        servSocket = new Socket().getServSocket();
        servSocket.on("join_room", join_room);
        servSocket.on("connect_room", connect_room);
        servSocket.on("send_message", send_message);
        servSocket.on("leave_room", leave_room);
        servSocket.on("me", me);
        servSocket.connect();

        sendBt.setOnClickListener(this);
        SharedPreferences sp = getContext().getSharedPreferences("LastSave", getContext().MODE_PRIVATE);
        Gson gsonM = new Gson();
        String jsonM = sp.getString("historico", null);
        String jsonP = sp.getString("clients", null);
        boolean status= sp.getBoolean("status", false);

        if(status){
            Login login= (Login) ((AppCompatActivity)getActivity()).getSupportFragmentManager().findFragmentByTag("Login");
            ((AppCompatActivity)getActivity()).getSupportFragmentManager().beginTransaction().
                    detach(login).commit();

            Type typeM = new TypeToken<ArrayList<Mensagens>>(){}.getType();
            Type typeP = new TypeToken<ArrayList<Personagem>>(){}.getType();
            mensagens= (gsonM.fromJson(jsonM, typeM));
            ArrayList<Personagem> p = (gsonM.fromJson(jsonP, typeP));
            CLIENTE_NAME.setNome(p.get(0).getNome());
            CLIENTE_NAME.setImagem(p.get(0).getImagem());
            CLIENTE_FRIEND = p.get(1);

            String room = mensagens.get(0).getRoom();

            mAdapter = new CustomChat(getContext(), mensagens);
            recyclerView.setAdapter(mAdapter);
            scrollToBottom();

            ((AppCompatActivity)getActivity()).setTitle("Room "+room);
            servSocket.emit("connect_room", CLIENTE_NAME.getNome(), room);

        }
        else{
            servSocket.emit("me");
            CLIENTE_NAME = Personagens.getInstance().getNomeMe();

            mAdapter = new CustomChat(getContext(), mensagens);
            recyclerView.setAdapter(mAdapter);

            servSocket.emit("join_room", CLIENTE_NAME.getNome());
        }
        return v;
    }

    private Emitter.Listener me = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(args[0].toString());
                        CLIENTE_NAME.set_id(data.getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };


    private Emitter.Listener join_room = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(args[0].toString());
                        String room = data.getString("room");
                        String _id = data.getString("_id");
                        String cliente_nome= data.getString("client_nome");
                        String dia= data.getString("dia");
                        String hora = data.getString("hora");
                        String msg = data.getString("msg");


                        if(_id.equals(CLIENTE_NAME.get_id())) {
                            mensagens.add(new Mensagens(room, CLIENTE_NAME, dia, hora, msg, -1));
                        }

                        else {
                            Personagem t = Personagens.getInstance().getNomeFriend(cliente_nome);
                            CLIENTE_FRIEND.setNome(t.getNome());
                            CLIENTE_FRIEND.set_id(t.get_id());
                            CLIENTE_FRIEND.setImagem(t.getImagem());
                            mensagens.add(new Mensagens(room, CLIENTE_FRIEND, dia, hora, msg, -1));
                        }

                        toolbar.setTitle("Room "+room);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mAdapter.notifyItemInserted(mensagens.size() - 1);
                    scrollToBottom();
                }
            });
        }
    };

    private Emitter.Listener connect_room = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(args[0].toString());
                        CLIENTE_NAME.set_id(data.getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };


    private Emitter.Listener send_message = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(args[0].toString());
                        String room = data.getString("room");
                        String _id = data.getString("_id");
                        String cliente= data.getString("client_nome");
                        String dia= data.getString("dia");
                        String hora = data.getString("hora");
                        String msg = data.getString("msg");

                        Log.v("send", CLIENTE_NAME.get_id() +" " +CLIENTE_FRIEND.get_id() + " "+_id);
                        if(_id.equals(CLIENTE_NAME.get_id()))
                            mensagens.add(new Mensagens(room, CLIENTE_NAME, dia, hora, msg, 1));
                        else
                            mensagens.add(new Mensagens(room, CLIENTE_FRIEND, dia, hora, msg, -1));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mAdapter.notifyItemInserted(mensagens.size() - 1);
                    scrollToBottom();
                }
            });

        }
    };

    private Emitter.Listener leave_room = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = new JSONObject(args[0].toString());
                        String room = data.getString("room");
                        String _id = data.getString("_id");
                        String cliente= data.getString("client_nome");
                        String dia= data.getString("dia");
                        String hora = data.getString("hora");
                        String msg = data.getString("msg");

                        mensagens.add(new Mensagens(room, CLIENTE_FRIEND, dia, hora, msg, -1));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mAdapter.notifyItemInserted(mensagens.size() - 1);
                    scrollToBottom();
                }
            });

        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menutoolbar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                Login login= (Login) ((AppCompatActivity)getContext()).getSupportFragmentManager().findFragmentByTag("Login");

                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().
                        attach(login).commit();

                STATUS_SAVE = false;
                servSocket.emit("leave_room");
                mensagens.clear();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scrollToBottom() {
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void salvarSecao(Boolean status){
        SharedPreferences sp = getContext().getSharedPreferences("LastSave", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Gson gsonM = new Gson();
        ArrayList<Personagem> p = new ArrayList<>();
        p.add(CLIENTE_NAME);
        p.add(CLIENTE_FRIEND);
        String jsonMsg = gsonM.toJson(mensagens);
        String clients = gsonM.toJson(p);

        editor.putString("historico", jsonMsg);
        editor.putString("clients", clients);
        editor.putBoolean("status", status);
        editor.commit();
    }

    @Override
    public void onPause() {
        salvarSecao(STATUS_SAVE);
        super.onPause();
    }

    @Override
    public void onStop() {
        salvarSecao(STATUS_SAVE);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        servSocket.disconnect();
        servSocket.off("join_room", join_room);
        servSocket.off("send_message", send_message);
        servSocket.off("leave_room", leave_room);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendBt:
                if(sendTv.length()>0){
                    String texto= sendTv.getText().toString();
                    servSocket.emit("send_message",texto);
                    sendTv.setText("");
                }
                break;
            default:
        }
    }
}