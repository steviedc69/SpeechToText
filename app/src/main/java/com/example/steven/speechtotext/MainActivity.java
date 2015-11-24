package com.example.steven.speechtotext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener{

    private ListView lv;
    private static final int CHECK = 111;
    private static String[]texts = {"Waaat's up Gangsta??","Yo Mama!","Daaammmn, Son","Who's bad?","Yo mama so fat, she got more chins than a hong kong phonebook"};
    private TextToSpeech ttx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.txtText);
        Button b = (Button)findViewById(R.id.btnGoGoGo);

        b.setOnClickListener(this);

        Button t = (Button)findViewById(R.id.btnTalk);
        t.setOnClickListener(this);

        ttx = new TextToSpeech(this,new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                     if (status != TextToSpeech.ERROR)
                     {
                         ttx.setLanguage(Locale.US);
                     }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnGoGoGo : this.openRecognistionIntent();
                break;
            case R.id.btnTalk :this.speakBack();
                break;
        }
    }
    public void speakBack()
    {
        Random r = new Random();
        String rand = texts[r.nextInt(texts.length)];
        ttx.speak(rand,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onResume() {
        if (ttx == null)
        {
            
            ttx = new TextToSpeech(this,new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR)
                    {
                        ttx.setLanguage(Locale.US);
                    }
                }
            });
        }



        super.onResume();
    }

    @Override
    protected void onPause() {
        if(ttx != null)
        {
            ttx.stop();
            ttx.shutdown();
        }
        super.onPause();
    }

    public void openRecognistionIntent()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak up son!");
        startActivityForResult(i, CHECK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == CHECK && resultCode == RESULT_OK)
        {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, results));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
