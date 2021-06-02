package com.mand.lubris;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Method;
import java.util.Map;


public class MenuFragment  extends Fragment {

    private EditText nick;
    private EditText pass;
    private SharedPreferences myPrefs;
    private View view;
    private TextView czyzalogowany;
    private ProgressBar bar;
    private  MainActivity mainActivity;

    private String data_nick;
    private String data_pass;

    private Map<String,String> firstCookie; //ciasteczka pobieranie od pierwszego wejscia na strone i potrzebne by się zalogować

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainActivity = new MainActivity();

        view = inflater.inflate(R.layout.fragment_menu, container, false);

        nick = (EditText)view.findViewById(R.id.nick_input);
        pass = (EditText)view.findViewById(R.id.pass_input);
        czyzalogowany = (TextView)view.findViewById(R.id.zalogowanyText);
        bar = (ProgressBar)view.findViewById(R.id.progresbar);
        Button btn = (Button)view.findViewById(R.id.accpet_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zapisz(v);
            }
        });

        //odczyt danych

        data_nick = mainActivity.read("nickname","Default");
        data_pass =  mainActivity.read("password","Default");

        //jeśli już został zalogowany to nic nie rób
        if(MainActivity.cookie!=null)
        {
            czyzalogowany.setText("Zalogowany");
        }else if(!data_nick.equals("") && !data_nick.equals("Default") && !data_pass.equals("")&& !data_pass.equals("Default"))
        {
            //automatyczne logowanie
            zaloguj(data_nick,data_pass);

        }
        //wstaw do inputa
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


        mainActivity.save("nickname", nick.getText().toString());
        mainActivity.save("password", pass.getText().toString());
        data_nick = nick.getText().toString();
        data_pass = pass.getText().toString();
        zaloguj(data_nick,data_pass);
    }


    //logowanie
    public void zaloguj(String nick, String pass)
    {
        new Thread()
        {


            @Override
            public void run() {

                    ProgressBar bar = (ProgressBar)view.findViewById(R.id.progresbar);
                    bar.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setVisibility(View.VISIBLE);
                        }
                    });

                    Connection.Response first = mainActivity.request(Jsoup.connect("https://api.librus.pl/OAuth/Authorization?client_id=46&response_type=code&scope=mydata")
                                                    .method(Connection.Method.GET));


                    Connection.Response login = mainActivity.request(Jsoup.connect("https://api.librus.pl/OAuth/Authorization?client_id=46")
                                                        .data("action","login")
                                                        .data("login",nick)
                                                        .data("pass",pass)
                                                        .cookies(first.cookies())
                                                        .ignoreContentType(true)
                                                        .method(Connection.Method.POST));

                    Connection.Response mainCokie = mainActivity.request(Jsoup.connect("https://api.librus.pl/OAuth/Authorization/Grant?client_id=46")
                                                        .method(Connection.Method.GET)
                                                        .cookies(first.cookies()));


                    MainActivity.cookie = mainCokie.cookies();
                    bar.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setVisibility(View.INVISIBLE);
                            czyzalogowany.setText("Zalogowany");
                        }
                    });


            }
        }.start();

    }
}
