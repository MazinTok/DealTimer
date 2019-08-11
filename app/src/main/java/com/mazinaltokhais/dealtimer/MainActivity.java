package com.mazinaltokhais.dealtimer;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.NumberPicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity  implements OnClickListener {

    private Chronometer chronometer;
    long timeWhenStopped = 0;
    boolean status = true;
    long TaregetTime = 0;
    long CurrentTime = 0;
    int TimeValue;

    NumberPicker NumP;
    Boolean MuteFlag = false;

    Button start_button;// = ((Button) findViewById(R.id.start_button));
    Button stop_button;//=  ((Button) findViewById(R.id.stop_button));
    Button reset_button;//= ((Button) findViewById(R.id.reset_button));
    Button Vol_button;//= ((Button) findViewById(R.id.Volbutton));
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        NumP = (NumberPicker) findViewById(R.id.numberPicker);
        NumP.setMinValue(0);
        String[] stringArray = new String[19];
        int n = 0;
        for (int i = 0; i < 19; i++) {
            stringArray[i] = Integer.toString(n);
            n += 5;
        }
        NumP.setMaxValue(stringArray.length - 1);
        NumP.setMinValue(0);
        NumP.setDisplayedValues(stringArray);
        //NumP.setWrapSelectorWheel(false);

        TimeValue = 0;
        NumP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // do your other stuff depends on the new value
                TimeValue = NumP.getValue() * 5;
            }
        });

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        start_button = ((Button) findViewById(R.id.start_button));
        stop_button = ((Button) findViewById(R.id.stop_button));
        reset_button = ((Button) findViewById(R.id.reset_button));
        Vol_button = ((Button) findViewById(R.id.Volbutton));
        // Button start_button = ((Button) findViewById(R.id.start_button));
        start_button.setOnClickListener(this);

        //Button stop_button =  ((Button) findViewById(R.id.stop_button));
        stop_button.setOnClickListener(this);

//       Button reset_button = ((Button) findViewById(R.id.reset_button));
        reset_button.setOnClickListener(this);

        //      Button Vol_button = ((Button) findViewById(R.id.Volbutton));
        Vol_button.setOnClickListener(this);


        //setImageResource

        mAdView = (AdView)findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Runnable stopClock = new Runnable() {

        @Override
        public void run() {
            chronometer.stop();
            //Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            //vib.vibrate(100);
            if (!MuteFlag)
                PlaySound();

            TimerLoop();
        }
    };


    public void TimerLoop() {

        //chronometer.removeCallbacks(stopClock);
        chronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
        Log.d("dd", String.valueOf(SystemClock.elapsedRealtime()));
        Log.d("dd", String.valueOf(timeWhenStopped));
        chronometer.start();
        if (timeWhenStopped != 0) {
            chronometer.postDelayed(stopClock, (1000 * TimeValue) - timeWhenStopped);
            timeWhenStopped = 0;
        } else {
            chronometer.postDelayed(stopClock, 1000 * TimeValue);
        }


    }

    public void loop() {

        final int delay = 5000; // delay for 5 sec.

        final int period = 1000; // repeat every sec.

        chronometer.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
        Log.d("dd", String.valueOf(SystemClock.elapsedRealtime()));
        Log.d("dd", String.valueOf(timeWhenStopped));
        chronometer.start();
        timeWhenStopped = 0;
        TaregetTime = (TimeValue * 1000) + SystemClock.elapsedRealtime();
        while (TaregetTime > SystemClock.elapsedRealtime()) {


        }
        loop();

    }

    /*
     runnable.run();

     private Runnable runnable = new Runnable()
     {

         public void run()
         {
             //
             // Do the stuff
             //

             handler.postDelayed(this, 1000);
         }
     };
     */
    @Override
    public void onClick(View v) {
        if (TimeValue != 0)
            switch (v.getId()) {
                case R.id.start_button:
                    status = true;
                    chronometer.removeCallbacks(stopClock);
                    TimerLoop();
                    start_button.setBackgroundResource(R.drawable.commandrefresh2);

                    break;
                case R.id.stop_button:

                    chronometer.stop();
                    chronometer.removeCallbacks(stopClock);
                    if (status) {
                        timeWhenStopped = SystemClock.elapsedRealtime() - chronometer.getBase();
                        start_button.setBackgroundResource(R.drawable.mediaplay2);
                    }
                    status = false;
                    break;
                case R.id.reset_button:
                    status = false;
                    // chronometer.setFormat(null);
                    chronometer.stop();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timeWhenStopped = 0;
                    break;
                case R.id.Volbutton:
                    if (MuteFlag) {
                        Vol_button.setBackgroundResource(R.drawable.volumespeaker2);
                        MuteFlag = false;
                    } else {
                        Vol_button.setBackgroundResource(R.drawable.volumemute2);
                        MuteFlag = true;
                    }


                    break;

            }
    }


    public void PlaySound() {


        try {
            AssetFileDescriptor afd = getAssets().openFd("doorbill.wav");

            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}