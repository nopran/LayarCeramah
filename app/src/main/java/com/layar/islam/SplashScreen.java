package com.layar.islam;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.os.Handler;


public class SplashScreen extends Activity {
    private int progressStatus = 0;
    private int sudah = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Get the widgets reference from XML layout
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        final TextView tv = (TextView) findViewById(R.id.tv);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);

        progressStatus = 0;
        //sudah = 0;


                /*
                    A Thread is a concurrent unit of execution. It has its own call stack for
                    methods being invoked, their arguments and local variables. Each application
                    has at least one thread running when it is started, the main thread,
                    in the main ThreadGroup. The runtime keeps its own threads
                    in the system thread group.
                */
        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
            /*    if (sudah > 0){
                    progressStatus = 100;
                    tv.setText(progressStatus+"0");
                    Intent maininten = new Intent(SplashScreen.this, Activity_Home.class);
                    startActivity(maininten);
                }*/

                while(progressStatus < 100){
                    // Update the progress status
                    progressStatus +=1;

                    // Try to sleep the thread for 20 milliseconds
                    try{
                        Thread.sleep(50);

                        if (progressStatus == 100){
                            //  sudah = sudah+1;
                            Intent maininten = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(maininten);

                        }

                    }catch(InterruptedException e){
                        e.printStackTrace();

                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                            // Show the progress on TextView
                            tv.setText(progressStatus+"");

                        }
                    });
                }
            }
        }).start(); // Start the operation

    }

    @Override
    protected void onStop() {
        Intent maininten = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(maininten);
        super.onStop();

    }
}