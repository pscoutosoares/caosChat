package com.android.chat.chat.Main;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chat.chat.Chat.Chat;
import com.android.chat.chat.Login.Login;
import com.android.chat.chat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;


import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("LastSave", getApplicationContext().MODE_PRIVATE);
        boolean status= sp.getBoolean("status", false);
        String room= sp.getString("nome", null);

        getSupportFragmentManager().beginTransaction().
                add(R.id.fragment_main, new Login(), "Login").addToBackStack("Login").commit();
        if(status){
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_main, new Chat(), "Chat").addToBackStack("Chat").commit();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private String getFragmentAtual(){
        return getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
    }

    private void clearFragments(){
        for(int i=1 ; i < getSupportFragmentManager().getBackStackEntryCount(); i++){
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
