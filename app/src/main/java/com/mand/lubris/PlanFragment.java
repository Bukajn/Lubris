package com.mand.lubris;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayList;

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
                mainActivity.pobierzPlanLekcji("2021-05-24");
                //mainActivity.pobierzSale();

                JSONObject plan=null;
                JSONObject sale=null;
                try {
                    plan= new JSONObject(mainActivity.read("timeTable2021-05-24","Default"));
                    sale = new JSONObject(mainActivity.read("classroom","Default"));
                    System.out.println(sale);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i=0; i<=9;i++) {
                    try {

                        JSONArray info = plan.getJSONObject("Timetable").getJSONArray("2021-05-24").getJSONArray(i);


                            getChildFragmentManager().beginTransaction().add(R.id.plan_miejsce,przygotujLekcje(info)).commit();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        return view;
    }


    private lesson_fragment przygotujLekcje(JSONArray info) throws JSONException {
        JSONObject inf = info.getJSONObject(0);

        String status=null;
        String lekcja=null;
        String nr=null;
        String nauczyciel=null;
        //status
        try
        {
            info.getJSONObject(1);
            status = "PrzesunięcieTU";
        }catch (JSONException e)
        {
            String odwolaneS = inf.get("IsCanceled").toString();
            String zastepstwoS = inf.get("IsSubstitutionClass").toString();
            boolean odwolane=false;
            boolean zastepstwo=false;
            if(odwolaneS.equals("true")){odwolane=true;}
            if(zastepstwoS.equals("true")){zastepstwo=true;}

            if(odwolane && zastepstwo){status = "PrzesunięcieZ";}
            else if(odwolane){status="Odwołane";}
            else if (zastepstwo){status="Zastępstwo";}

        }


        if (status==null) {
            lekcja = inf.getJSONObject("Subject").get("Name").toString();
            nr = inf.get("LessonNo").toString();
            nauczyciel = inf.getJSONObject("Teacher").get("LastName").toString()+" "+inf.getJSONObject("Teacher").get("FirstName").toString();
        }


        return new lesson_fragment(status,lekcja,nr,nauczyciel);
    }
}
