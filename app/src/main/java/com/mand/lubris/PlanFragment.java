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
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlanFragment extends Fragment {
    private MainActivity mainActivity;
    Map<String, String> godziny = new HashMap<String, String>();
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
                //System.out.println(godziny.toString());

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date =  dateFormat.format(calendar.getTime());


                dateFormat = new SimpleDateFormat("HH");
                Integer hour = Integer.parseInt(dateFormat.format(calendar.getTime()));

                if (hour>=15)
                {
                    Integer dzisiaj = Integer.parseInt(date.substring(8,10));
                    dzisiaj+=1;
                    date = date.substring(0,8);
                    if (dzisiaj<10)
                    {
                        date = date+"0"+dzisiaj.toString();
                    }else {
                        date = date+dzisiaj.toString();
                        }


                }


                System.out.println(hour);
                System.out.println(date);
                for(Element elem: document.select("#timetableEntryBox"))
                {
                    //data-date
                    //2021-05-21
                    if (!elem.attr("data-date").equals(date))
                    {
                       elem.remove();
                    }
                }

                Elements plan = document.select("#timetableEntryBox");


                for (Element elem: plan)
                {

                    getChildFragmentManager().beginTransaction().add(R.id.plan_miejsce,stworzLekcje(elem)).commit();
                }

            }
        }.start();


        return view;
    }

    private lesson_fragment stworzLekcje(Element element)
    {
        String status =null;
        String lekcja =null;
        String nowaLekcja =null;
        String nr =null;
        String nauczyciel =null;
        String nowyNauczyciel=null;
        String sala =null;
        String nowaSala=null;


        try {
            status = element.selectFirst("div").text();
            if (status.contains("-"))
            {
                status=null;
            }

            //wykrywa czy jest przsuniecie na daną lekcję
            if(status.equals("odwołane")&&element.selectFirst("a > div").text().equals("przesunięcie"))
            {
                status = "PrzesunięcieTu";
            }

        }catch (NullPointerException e)
        {
           //nie ma

        }


        try {
            if(status==null)
            {
                lekcja = element.selectFirst("div > b").text();

                nauczyciel = element.selectFirst("div").text();
                nauczyciel = nauczyciel.substring(nauczyciel.indexOf("-")+1);

                //usuwa spację przed nazwiskiem
                if (nauczyciel.charAt(0) == ' ')
                {
                    nauczyciel = nauczyciel.substring(1);
                }

                int miescePierwszejSpacji = nauczyciel.indexOf(" ");
                nauczyciel = nauczyciel.substring(0,nauczyciel.indexOf(" ",miescePierwszejSpacji+1));

                sala= element.selectFirst("div").text();
                sala = sala.substring(sala.indexOf(".")+2);

                String przedzialCzasowy = element.attr("data-time_from")+" - " +element.attr("data-time_to");
                nr=godziny.get(przedzialCzasowy);
            }else if (status.equals("zastępstwo"))
            {
               String info = element.selectFirst("a").attr("title");

               String lekcjaZHTML = info.substring(info.indexOf("Przedmiot:"),info.indexOf("Sala:"));
               lekcja = lekcjaZHTML.substring(lekcjaZHTML.indexOf(" ")+1,lekcjaZHTML.indexOf("<br>"));

               nowaLekcja = element.selectFirst("div > b").text();

               if (lekcja.equals(nowaLekcja))
               {
                   nowaLekcja=null;
               }

               String saleZHtml = info.substring(info.indexOf("Sala:"),info.indexOf("Data dodania:"));
               sala = saleZHtml.substring(saleZHtml.indexOf(" ")+1,saleZHtml.indexOf("-"));

               nowaSala = saleZHtml.substring(saleZHtml.indexOf("->")+3,saleZHtml.indexOf("<br"));
               if (sala.equals(nowaSala) || nowaSala.equals("[brak]"))
               {
                   nowaSala=null;
               }


               String nauczycieleZHTML = info.substring(info.indexOf("Nauczyciel:"),info.indexOf("Przedmiot:"));
               nauczyciel = nauczycieleZHTML.substring(nauczycieleZHTML.indexOf(" ")+1,nauczycieleZHTML.indexOf("-"));
               nowyNauczyciel = nauczycieleZHTML.substring(nauczycieleZHTML.indexOf("->")+3,nauczycieleZHTML.indexOf("<br"));

               String przedzialCzasowy = element.attr("data-time_from")+" - " +element.attr("data-time_to");
               nr=godziny.get(przedzialCzasowy);

            }else if(status.equals("odwołane"))
            {

                lekcja = element.selectFirst("div > b").text();

                nauczyciel = element.selectFirst("s:eq(2)>s>div").text();
                nauczyciel = nauczyciel.substring(nauczyciel.indexOf("-")+1);


                String przedzialCzasowy = element.attr("data-time_from")+" - " +element.attr("data-time_to");
                nr=godziny.get(przedzialCzasowy);
            }else if(status.equals("przesunięcie"))
            {
                lekcja = element.selectFirst("div > b").text();

                nauczyciel = element.selectFirst("s:eq(2)>div").text();
                nauczyciel = nauczyciel.substring(nauczyciel.indexOf("-")+1);


                String przedzialCzasowy = element.attr("data-time_from")+" - " +element.attr("data-time_to");
                nr=godziny.get(przedzialCzasowy);
            }else if(status.equals("PrzesunięcieTu"))
            {
                String info = element.selectFirst("a").attr("title");

                lekcja =  element.selectFirst("div > b").text();

                nauczyciel = element.selectFirst("s:eq(2)>s>div").text();
                nauczyciel = nauczyciel.substring(nauczyciel.indexOf("-")+1);
                if (nauczyciel.charAt(0) == ' ')
                {
                    nauczyciel = nauczyciel.substring(1);
                }

                String nowalekcjaHTML = info.substring(info.indexOf("Przedmiot:"),info.indexOf("Sala:"));
                nowaLekcja = nowalekcjaHTML.substring(nowalekcjaHTML.indexOf("/b> ")+4,nowalekcjaHTML.indexOf("<br"));

                String nauczycieleZHTML = info.substring(info.indexOf("Nauczyciel:"),info.indexOf("Przedmiot:"));
                nowyNauczyciel = nauczycieleZHTML.substring(nauczycieleZHTML.indexOf("->")+3,nauczycieleZHTML.indexOf("<br"));

                String saleZHtml = info.substring(info.indexOf("Sala:"),info.indexOf("Data dodania:"));
                sala = saleZHtml.substring(saleZHtml.indexOf(" ")+1,saleZHtml.indexOf("-"));

                nowaSala = saleZHtml.substring(saleZHtml.indexOf("->")+3,saleZHtml.indexOf("<br"));

                String przedzialCzasowy = element.attr("data-time_from")+" - " +element.attr("data-time_to");
                nr=godziny.get(przedzialCzasowy);


            }

        }catch (NullPointerException e)
        {
            //nie ma

        }

       return new lesson_fragment(status,lekcja,nowaLekcja,nr,nauczyciel,nowyNauczyciel,sala,nowaSala);
    }

}
