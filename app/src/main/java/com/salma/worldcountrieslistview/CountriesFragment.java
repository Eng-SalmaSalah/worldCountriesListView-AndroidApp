package com.salma.worldcountrieslistview;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountriesFragment extends Fragment {

    List<Country> countryObjList;
    List<String>countriesNames;
    ReadJson readJson;
    ArrayAdapter arrayAdapter;
    ListView listView;

    public CountriesFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countryObjList = new ArrayList<>();
        countriesNames=new ArrayList<>();
        readJson = new ReadJson();
        Thread thread = new Thread(readJson);
        thread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_countries, container, false);
        listView=view.findViewById(R.id.countriesContainer);
        arrayAdapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,countriesNames);
        listView.setAdapter(arrayAdapter);
        return view;
    }

    private class ReadJson implements Runnable {
        URL url = null;
        HttpsURLConnection httpsURLConnection = null;
        InputStream inputStream = null;
        String response;

        @Override
        public void run() {
            handleHttpConnection();
            //Log.i("Done", "Done");
        }

        private void handleHttpConnection() {
            try {
                url = new URL("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                inputStream = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(inputStream);
                parseToJson(response);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void parseToJson(String response) {
            JSONObject jsonObject;
            JSONArray countries;
            if (response != null) {
                try {
                    jsonObject = new JSONObject(response);
                    countries = jsonObject.getJSONArray("worldpopulation");
                    for (int i = 0; i < countries.length(); i++) {
                        JSONObject countryObj = countries.getJSONObject(i);
                        String rank = countryObj.getString("rank");
                        String name = countryObj.getString("country");
                        String population = countryObj.getString("population");
                        String flagImgUrlStr = countryObj.getString("flag").replace("http", "https");
                        Country country = new Country(rank, name, population, flagImgUrlStr);
                        countryObjList.add(country);
                        countriesNames.add(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        private String convertStreamToString(InputStream inputStream) {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();
        }
    }


}

