package com.android.chat.chat.Chat.Nomes;


import com.android.chat.chat.R;

import java.util.ArrayList;
import java.util.Random;

public class Personagens {
    private ArrayList<Personagem> personagems = new ArrayList<>();
    private static final Personagens ourInstance = new Personagens();

    public static Personagens getInstance() {
        return ourInstance;
    }

    private Personagens() {
        personagems.add(new Personagem("Elefante", R.drawable.elefante));
        personagems.add(new Personagem("Zebra",R.drawable.zebra));
        personagems.add(new Personagem("Cachorro",R.drawable.cachorro));
        personagems.add(new Personagem("Gato",R.drawable.gato));
        personagems.add(new Personagem("Tigre",R.drawable.tigre));
        personagems.add(new Personagem("Crocodilo",R.drawable.jacare));
    }

    public Personagem getNomeMe(){
        Random random = new Random();
        int name_random = random.nextInt(personagems.size() - 0);
        return personagems.get(name_random);
    }

    public Personagem getNomeFriend(String name){
        for(int i = 0; i< personagems.size(); i++){
            if(personagems.get(i).getNome().equals(name))
                return personagems.get(i);
        }
        return null;
    }
}
