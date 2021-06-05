package com.mand.lubris;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlanFragment extends Fragment {
    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = new MainActivity();
        View view = inflater.inflate(R.layout.fragment_plan, container, false);










        new Thread()
        {
            @Override
            public void run()
            {
                Document document = Jsoup.parse(mainActivity.read("timetable","Default"));

                //tworzenie mapy numery lekcji i godzin
                Map<String, String> godziny = new HashMap<String, String>();
                for(int i=0;i<=100;i++)
                {
                    try
                    {
                        Element nr = document.selectFirst("#body>div>div>form>table:eq(3)>tbody>tr:eq("+(i*2)+")>th");
                        godziny.put(nr.text(),Integer.toString(i));
                        //*[@id="body"]/div/div/form/table[2]/tbody/tr[1]/th
                        //*[@id="body"]/div/div/form/table[2]/tbody/tr[2]/td[2]

                    }catch (NullPointerException e)
                    {

                        break;
                    }


                }
                System.out.println(godziny.toString());


                //*[@id="body"]/div/div/form/table[2]/tbody/tr[1]/td[1]
                //*[@id="body"]/div/div/form/table[2]/tbody/tr[3]/td[1]

            }
        }.start();


        return view;
    }



}
