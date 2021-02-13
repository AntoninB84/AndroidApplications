package fr.jampa.jampav2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class VerificationRappels extends JobService {
    private static final String TAG = "RappelScheduler";
    private SQLiteDataBaseHelper db = new SQLiteDataBaseHelper(this);
    String[] idT, titleT, dateLimT, rappelT, idR, titleR, dateLimR, idTA, titleTA, dateLimTA;
    @Override
    public boolean onStartJob(JobParameters params) {
        verifMAJ();
        progNotif();

        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        //Ce qu'il se passe à la fermeture du service ?
        return true;
    }

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, VerificationRappels.class);
        JobInfo jobInbo = new JobInfo.Builder(1, serviceComponent)
               .setPeriodic(1200000) // 1h = 3 600 000 ( 20min actually)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setMinimumLatency(0)
                .build();
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(jobInbo);
        Log.i("Infos", "Le JobScheduler a été initialisé !");
    }
    private void verifMAJ(){
        Log.i("Infos", "On est passé par verifMaj");
        db.renouvRecurFini(); // Recurrente finie, renouv automatique
        db.renouvQuotid();
        // Renouv recurrente pas finie
        Cursor RpasFini = db.renouvRecurPasFini();
        idR = new String[RpasFini.getCount()];
        titleR = new String[RpasFini.getCount()];
        dateLimR = new String[RpasFini.getCount()];
        if(RpasFini.moveToFirst()){
            int i = 0;
            do {
                idR[i] = String.valueOf(RpasFini.getInt(0));
                titleR[i] = RpasFini.getString(1);
                dateLimR[i] = RpasFini.getString(6);
                notificationDialog( "Avez-vous oublié ? : "+titleR[i], "Récurrente prévue à "+GestionDates.RecupHeure(dateLimR[i]),0, parseInt(idR[i]));
                i++;
            } while (RpasFini.moveToNext());
        }

        // Autres taches pas finies
        Cursor TpasFini = db.setNonEffectue();
        idTA= new String[TpasFini.getCount()];
        titleTA = new String[TpasFini.getCount()];
        dateLimTA = new String[TpasFini.getCount()];
        if(TpasFini.moveToFirst()){
            int i = 0;
            do {
                idTA[i] = String.valueOf(TpasFini.getInt(0));
                titleTA[i] = TpasFini.getString(1);
                dateLimTA[i] = TpasFini.getString(6);
                notificationDialog( "Avez-vous oublié ? : "+titleTA[i], "Prévu à "+GestionDates.RecupHeure(dateLimTA[i]), 0, parseInt(idTA[i]));
                i++;
            } while (TpasFini.moveToNext());
        }
    }
    private void progNotif(){
        Log.i("Infos", "On est passé par progNotif !");
        String dateActuelle = GestionDates.DateActuelle();
        Cursor data = db.recupTachesNotif(dateActuelle);
        Boolean besoinNotif;
        idT = new String[data.getCount()];
        titleT = new String[data.getCount()];
        dateLimT = new String[data.getCount()];
        rappelT = new String[data.getCount()];
        if (data.moveToFirst()) {
            int i = 0;
            do {
                idT[i] = String.valueOf(data.getInt(0));
                titleT[i] = data.getString(1);
                dateLimT[i] = data.getString(6);
                rappelT[i] = data.getString(8);
                besoinNotif = GestionDates.ComparaisonDates(dateLimT[i], rappelT[i]);
                if(besoinNotif == true){
                    long rappel = Long.parseLong(rappelT[i]);
                    Log.i("Infos", "besoinNotif == true! et rappel = "+rappelT[i]);
                    if(rappel <= 86000000){ // ~~1jour
                        notificationDialog("Tache : " + titleT[i], "Prévu à " + GestionDates.RecupHeure(dateLimT[i]), 0, parseInt(idT[i]));
                    }else {
                        notificationDialog("Tache : " + titleT[i], "Prévu pour le " + GestionDates.ChangerFormatDatePhrase(dateLimT[i]), 0, parseInt(idT[i]));
                    }
                }
                i++;
            } while (data.moveToNext());
        }
    }


    private void notificationDialog(String titre, String texte, int IdNotif, int idTache) {

        Intent resultIntent = new Intent(this, AffichageTache.class);
        resultIntent.putExtra("IdTache", idTache);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        String packet = this.getPackageName();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Jampa's Task Notification");
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher) // TODO ESSAYER DE CHANGER ICONE
                .setContentTitle(titre)
                .setContentText(texte)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setWhen(System.currentTimeMillis());

        notificationManager.notify(IdNotif, notificationBuilder.build());
    }


}
