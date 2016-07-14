package com.ezlife.betterdiction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button completeBtn;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCategorySpinner();

        /*tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                *//*tts.setLanguage(Locale.ENGLISH);
                Set<Locale> test = tts.getAvailableLanguages();
                for(Locale a : test) {
                    String ret = a.toString();
                    Log.d("HELLO", "List of Available Languages for TTS :: " + ret);

                }
                Log.d("HELLO", "Default Language is :: " + tts.getDefaultEngine().toString());
                Log.d("HELLO", "Default Language is :: " + tts.getDefaultLanguage().toString());
                String str = "How can I add the damn language?";
                tts.setLanguage(Locale.UK);
                tts.speak(str, TextToSpeech.QUEUE_ADD, null, null);*//*
            }
        });*/



    }

    public void onCompleteBtnClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), PracticeDictionActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, 1000);

    }

    private void setCategorySpinner() {
        /*
         * Category Spinner SETUP
         */

        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Category_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setScriptSpinner((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setScriptSpinner(String category) {
        /*
         * Script Spinner SETUP
         */

        int resId = R.array.Category_array;
        if(category.equals("Drama")){
            resId = R.array.Drama_array;
        }

        Spinner scriptSpinner = (Spinner) findViewById(R.id.scriptSpinner);

        adapter = ArrayAdapter.createFromResource(
                this,
                resId,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        scriptSpinner.setAdapter(adapter);

        scriptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSeasonSpinner();
                setEpisodeSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSeasonSpinner(){

        Spinner seasonSpinner = (Spinner) findViewById(R.id.seasonSpinner);

        List<String> list = new ArrayList<String>();

        for(int i = 0; i < 10; i++) {
            int a = i +1;
            String value = "Season " + a;
            list.add(value);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item, list
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seasonSpinner.setAdapter(dataAdapter);
    }

    private void setEpisodeSpinner(){

        Spinner episodeSpinner = (Spinner) findViewById(R.id.episodeSpinner);

        List<String> list = new ArrayList<String>();

        for(int i = 0; i < 24; i++) {
            int a = i +1;
            String value = "Episode " + a;
            list.add(value);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item, list
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        episodeSpinner.setAdapter(dataAdapter);
    }
}
