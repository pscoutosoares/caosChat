package com.android.chat.chat.Login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.chat.chat.Chat.Chat;
import com.android.chat.chat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment implements View.OnClickListener {

    private Button btEntrar;
    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        btEntrar= (Button) v.findViewById(R.id.btEntrar);

        btEntrar.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btEntrar:
                // verifica tudo e entra
                // ...


                Login login= (Login) ((AppCompatActivity)getContext()).getSupportFragmentManager().findFragmentByTag("Login");

                getActivity().getSupportFragmentManager().beginTransaction().
                        detach(login).commit();
                getActivity().getSupportFragmentManager().beginTransaction().
                        add(R.id.fragment_main, new Chat(), "Chat").addToBackStack("Chat").commit();
                break;
        }
    }
}
