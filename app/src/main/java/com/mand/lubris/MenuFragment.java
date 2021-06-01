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
        myPrefs= getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        data_nick = myPrefs.getString("nickname","Default");
        data_pass = myPrefs.getString("password","Default");

        //jeśli już został zalogowany to nic nie rób
        if(MainActivity.cookie!=null)
        {
            czyzalogowany.setText("Zalogowany");
        }else if(!data_nick.equals("") && !data_nick.equals("Default") && !data_pass.equals("")&& !data_pass.equals("Default"))
        {
            //automatyczne logowanie
            zaloguj();
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

        SharedPreferences pref = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        mainActivity.save("nickname", nick.getText().toString(),pref);
        mainActivity.save("password", pass.getText().toString(),pref);
        data_nick = nick.getText().toString();
        data_pass = pass.getText().toString();
        zaloguj();
    }

    //logowanie
    public void zaloguj()
    {

        bar.setVisibility(View.VISIBLE);
        mainActivity.reqeust(Jsoup.connect("https://api.librus.pl/OAuth/Authorization?client_id=46&response_type=code&scope=mydata")
                .method(Connection.Method.GET),this,"zaloguj_etap2");
    }

    public void zaloguj_etap2(Connection.Response res)
    {

        mainActivity.reqeust(Jsoup.connect("https://api.librus.pl/OAuth/Authorization?client_id=46")
                .data("action","login")
                .data("login",data_nick)
                .data("pass",data_pass)
                .cookies(res.cookies())
                .ignoreContentType(true)
                .method(Connection.Method.POST),this,"zaloguj_etap3");
                firstCookie = res.cookies();
    }

    public void zaloguj_etap3(Connection.Response res)
    {
        mainActivity.reqeust(Jsoup.connect("https://api.librus.pl/OAuth/Authorization/Grant?client_id=46")
                .method(Connection.Method.GET)
                .cookies(firstCookie),this,"zaloguj_etap4");
    }

    public void zaloguj_etap4(Connection.Response res)
    {
        MainActivity.cookie = res.cookies();
        bar.setVisibility(View.INVISIBLE);
        czyzalogowany.setText("Zalogowany");
    }
    //logowanie
    /*public void zaloguj()
    {
        new Thread()
        {


            @Override
            public void run() {
                try {
                    ProgressBar bar = (ProgressBar)view.findViewById(R.id.progresbar);
                    bar.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setVisibility(View.VISIBLE);
                        }
                    });

                    Connection.Response first = Jsoup.connect("https://api.librus.pl/OAuth/Authorization?client_id=46&response_type=code&scope=mydata")
                                                    .method(Connection.Method.GET)
                                                    .execute();

                    Connection.Response login = Jsoup.connect("https://api.librus.pl/OAuth/Authorization?client_id=46")
                                                        .data("action","login")
                                                        .data("login",nick)
                                                        .data("pass",pass)
                                                        .cookies(first.cookies())
                                                        .ignoreContentType(true)
                                                        .method(Connection.Method.POST)
                                                        .execute();

                    Connection.Response mainCokie = Jsoup.connect("https://api.librus.pl/OAuth/Authorization/Grant?client_id=46")
                                                        .method(Connection.Method.GET)
                                                        .cookies(first.cookies())
                                                        .execute();


                    MainActivity.cookie = mainCokie.cookies();
                    bar.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setVisibility(View.INVISIBLE);
                            czyzalogowany.setText("Zalogowany");
                        }
                    });



                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();

    }*/
}
