package fr.jampa.jampav2;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TimePicker;

public class ActiviteSportive extends Activity {
    Button go_stop,btn_stop, btn_chrono;
    long duree, repos;
    int i;
    EditText minuteur_minutes, minuteurs_secondes, intervalle_minutes, intervalle_seconds;
    private CountDownTimer TempsMinuteur, TempsIntervalle;

    /**
     * This is a little side-project. When you click on the title of a sport category task, this chronometer activity appears.
     * This one will not make it to the next version and will instead be remade in another sport-oriented application.
     * **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_sportive);
        final Chronometer simpleChrono =(Chronometer)findViewById(R.id.chrono);
        btn_chrono=(Button)findViewById(R.id.btn_lancer_chrono);
        i =0;
        btn_chrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i%2 != 0){
                    simpleChrono.start();
                    i++;
                }else{
                    simpleChrono.stop();
                    i++;
                }
            }
        });


        minuteur_minutes=(EditText)findViewById(R.id.et_minuteur_minutes);
        minuteurs_secondes=(EditText)findViewById(R.id.et_minuteur_secondes) ;
        intervalle_minutes=(EditText)findViewById(R.id.et_intervale_minutes);
        intervalle_seconds=(EditText)findViewById(R.id.et_intervale_secondes);
        final MediaPlayer mp = MediaPlayer.create(ActiviteSportive.this, R.raw.bip);
        go_stop=(Button)findViewById(R.id.btn_lancer_minuteur);
        btn_stop=(Button)findViewById(R.id.btn_stopper_minuteur);
        go_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duree = Long.parseLong(minuteur_minutes.getText().toString())*60000+Long.parseLong(minuteurs_secondes.getText().toString())*1000;
                repos = Long.parseLong(intervalle_minutes.getText().toString())*60000+Long.parseLong(intervalle_seconds.getText().toString())*1000;
                TempsMinuteur = new CountDownTimer(duree, 1000) {
                    public void onTick(long millisUntilFinished) {
                        minuteur_minutes.setText( String.valueOf(millisUntilFinished / 60000));
                        minuteurs_secondes.setText(String.valueOf((millisUntilFinished%60000)/1000));
                        minuteurs_secondes.setEnabled(false); minuteur_minutes.setEnabled(false);
                        intervalle_seconds.setEnabled(false); intervalle_minutes.setEnabled(false);
                        if(millisUntilFinished<=6000){
                            mp.start();
                        }
                    }
                    public void onFinish() {
                        TempsIntervalle = new CountDownTimer(repos, 1000) {
                            public void onTick(long millisUntilFinished) {
                                intervalle_minutes.setText( String.valueOf(millisUntilFinished / 60000));
                                intervalle_seconds.setText(String.valueOf((millisUntilFinished%60000)/1000));
                                intervalle_seconds.setEnabled(false); intervalle_minutes.setEnabled(false);
                                minuteurs_secondes.setEnabled(false); minuteur_minutes.setEnabled(false);
                                if(millisUntilFinished<=6000){
                                  mp.start();
                                }
                            }
                            public void onFinish() {
                                TempsMinuteur.start();
                            }
                        }.start();
                    }
                }.start();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TempsMinuteur.cancel(); TempsIntervalle.cancel();
                intervalle_seconds.setEnabled(true); intervalle_minutes.setEnabled(true);
                minuteurs_secondes.setEnabled(true); minuteur_minutes.setEnabled(true);
            }
        });

    }
}
