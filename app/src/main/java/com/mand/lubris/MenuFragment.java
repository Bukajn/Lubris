package com.mand.lubris;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class MenuFragment  extends Fragment {

    private EditText nick;
    private EditText pass;
    private SharedPreferences myPrefs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        nick = (EditText)view.findViewById(R.id.nick_input);
        pass = (EditText)view.findViewById(R.id.pass_input);

        Button btn = (Button)view.findViewById(R.id.accpet_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zapisz(v);
            }
        });

        myPrefs= getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        String data_nick = myPrefs.getString("nickname","Default");
        String data_pass = myPrefs.getString("password","Default");

        if(!data_nick.equals("Default")){
            nick.setText(data_nick);
        }
        if(!data_pass.equals("Default")){
            pass.setText(data_pass);
        }

        return view;
    }

    public void zapisz(View v)
    {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("nickname", nick.getText().toString());
        editor.putString("password", pass.getText().toString());
        editor.apply();
        editor.commit();

    }

    public void zaloguj(String nick, String pass)
    {

        //Connection.Response first = Jsoup.connect("https://api.librus.pl/OAuth/Authorization?client_id=46&response_type=code&scope=mydata")
                                            //.method(Connection.Method.GET).execute();
    }
}
