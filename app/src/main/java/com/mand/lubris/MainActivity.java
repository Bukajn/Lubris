package com.mand.lubris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


   private DrawerLayout drawer;
   public static Map<String,String> cookie;
   public static Context context;
   public FragmentManager fragmentManager;

   private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawewr_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DrawerArrowDrawable arrow = toggle.getDrawerArrowDrawable();
        arrow.setColor(getResources().getColor(R.color.white));

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlanFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_plan);
        }

        context = this;
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MenuFragment()).commit();
                break;
            case R.id.nav_plan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PlanFragment()).commit();
                break;
            case R.id.nav_ogloszenie:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new OgloszeniaFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void zmienNaPlan()
    {
        fragmentManager.beginTransaction().replace(R.id.fragment_container,new PlanFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_plan);
    }

    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }


    //zapisywanie
    public void save(String key, String data)
    {
        SharedPreferences myPrefs = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString(key, data);
        editor.apply();
        editor.commit();

    }

    public String read(String key,String def)
    {
        SharedPreferences myPrefs = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        return myPrefs.getString(key,def);
    }

    public Connection.Response request(Connection con)
    {
        Connection.Response res=null;
        try {
           res = con.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }






    public void pobierzPlanLekcji() {
        Connection.Response res;

        res =  request(Jsoup.connect("https://synergia.librus.pl/przegladaj_plan_lekcji")
                .cookies(cookie)
                .method(Connection.Method.GET));

        Document document = null;
        try {
            document = res.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element element = document.selectFirst("#body>div>div>form>input");
        String value = element.attr("value");

        res = request(Jsoup.connect("https://synergia.librus.pl/przegladaj_plan_lekcji")
                                    .data("requestkey",value)
                                    .data("tydzien","2021-05-17_2021-05-23")
                                    .data("pokaz_zajecia_zsk","on")
                                    .data("pokaz_zajecia_ni","on")
                                    .cookies(cookie)
                                    .method(Connection.Method.POST));
        try {
            document = res.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

       save("timetable",document.toString());
    }



}