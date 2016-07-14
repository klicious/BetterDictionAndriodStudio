package com.ezlife.betterdiction;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kwon on 2016-07-15.
 */
public class PracticeDictionActivity extends AppCompatActivity {

    TextView sampleText;
    TextView translation;
    EditText userInput;
    TextView resultText;

    TextToSpeech tts;

    ArrayList<String> script;
    ArrayList<ArrayList<String>> eachScript;
    String[] cast = {"Rachel", "Ross", "Monica", "Joey", "Phoebe", "Chandler", "Everyone"};
    int castIndex;
    int[] scriptIndex, scriptIndexMax;

    public enum friends {
        Rachel(0), Ross(1), Monica(2), Joey(3), Phoebe(4), Chandler(5), Everyone(6);

        private int frdNo;

        private static Map<Integer, friends> map = new HashMap<Integer, friends>();

        static {
            for (friends frdEnum : friends.values()){
                map.put(frdEnum.frdNo, frdEnum);
            }
        }

        private friends(final int frd) { frdNo = frd; }

        public static friends valueOf(int frdNo) {
            return map.get(frdNo);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practicediction);

        Intent intent = getIntent();

        sampleText = (TextView) findViewById(R.id.sampleContentTextView);
        translation = (TextView) findViewById(R.id.translationContentTextView);
        userInput = (EditText) findViewById(R.id.userInputContentEditText);
        resultText = (TextView) findViewById(R.id.resultTextView);

        script = new ArrayList<String>();
        ArrayList<String> fullScript = new ArrayList<String>();
        ArrayList<String> Rachel = new ArrayList<String>();
        ArrayList<String> Ross = new ArrayList<String>();
        ArrayList<String> Monica = new ArrayList<String>();
        ArrayList<String> Joey = new ArrayList<String>();
        ArrayList<String> Phoebe = new ArrayList<String>();
        ArrayList<String> Chandler = new ArrayList<String>();
        eachScript = new ArrayList<ArrayList<String>>();

        eachScript.add(Rachel);
        eachScript.add(Ross);
        eachScript.add(Monica);
        eachScript.add(Joey);
        eachScript.add(Phoebe);
        eachScript.add(Chandler);
        eachScript.add(fullScript);

        try {
            fullScript = OpenFile("Drama/Friends/Season1/Friendstranscript101.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String ret : fullScript) {
            boolean isCast = false;

            String[] parts = ret.split(":");

            for(String name : cast) {
                //Log.d("HELLO", parts[0]);
                //Log.d("HELLO", parts[1]);
                if (name.equals(parts[0])){
                    isCast = true;

                    eachScript.get(
                            friends.valueOf(name).ordinal())
                            .add(parts[1].replaceAll("\\(.*\\)", "").replaceAll("\\[.*\\]", "").trim());

                    break;
                }

            }

        }

        Comparator<String> x =  new Comparator<String>() {
            @Override
            public int compare (String o1, String o2){
                if (o1.length() > o2.length())
                    return 1;
                if (o1.length() < o2.length())
                    return -1;
                return 0;
            }
        };
        scriptIndex = new int[eachScript.size()];
        scriptIndexMax = new int[eachScript.size()];

        Log.d("HELLO", "eachScript.size() = " + eachScript.size() + " :: scriptIndex.length() = " + scriptIndex.length + " :: scripIndexMax.length() = " + scriptIndexMax.length);

        for (int i = 0 ; i < eachScript.size(); i++){
            Collections.sort(eachScript.get(i), x);
            scriptIndex[i] = 0;
            scriptIndexMax[i] = eachScript.get(i).size();
            Log.d("HELLO", "eachScript.get(" + i + ").size() = " + eachScript.get(i).size());
        }

        setFavCharSpinner();

    }

    private ArrayList<String> OpenFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(path), "UTF-8"));

        ArrayList<String> lines = new ArrayList<String>();

        String line;
        while((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        return lines;

    }

    public void onReadSampleTextBtnClicked(View v) {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                String str = sampleText.getText().toString();

                tts.setLanguage(Locale.ENGLISH);
                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    public void onReadTranslationTextBtnClicked(View v) {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                String str = translation.getText().toString();

                tts.setLanguage(Locale.UK);
                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    public void onReadUserInputTextBtnClicked(View v) {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                String str = userInput.getText().toString();

                tts.setLanguage(Locale.CANADA);
                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    public void onCompleteBtnClicked(View v) {
        String s = "",t = "";
        s = sampleText.getText().toString();
        t = userInput.getText().toString();
        String result = StringSimilarity.getSimilarity(s, t);

        resultText.setText(result);

        if (scriptIndex[castIndex] < scriptIndexMax[castIndex] - 1){
            scriptIndex[castIndex]++;
        } else {
            castIndex++;
        }

        if (castIndex >= scriptIndex.length) {
            castIndex = 0;
        }

        sampleText.setText(eachScript.get(castIndex).get(scriptIndex[castIndex]));

    }

    private void setFavCharSpinner() {
        /*
         * Favorite Character Spinner SETUP
         */

        Spinner favCharSpinner = (Spinner) findViewById(R.id.favCharSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Friends_casts,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        favCharSpinner.setAdapter(adapter);

        favCharSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String favChar = (String) parent.getItemAtPosition(position);
                castIndex = friends.valueOf(favChar).ordinal();
                sampleText.setText(eachScript.get(castIndex).get(scriptIndex[castIndex]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
