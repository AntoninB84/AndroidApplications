package fr.jampa.jampav2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity {
    SQLiteDataBaseHelper db;
    ListView listviewMain;
    public String[] IDi={}, TITLEi={}, DESCRi={}, PRIOi={}, FAITi={}, DATELi={};

    /**
     * This application is the first that I made. It is very messy and not quite optimized.
     * I intend to remake it, object-oriented, and with the new methods I learned.
     * **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //VerificationRappels.scheduleJob(this);

        db = new SQLiteDataBaseHelper(this) ;
        RecupererTaches();
        listviewMain =(ListView)findViewById(R.id.lv_main);
        listviewMain.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            public void onSwipeLeft(){
                Intent goSettings = new Intent(MainActivity.this, SettingsTriTache.class);
                startActivityIfNeeded(goSettings, 0);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
            public void onSwipeRight(){
                Intent goMenu = new Intent(MainActivity.this, CreationTache.class);
                goMenu.putExtra("Action", "Creation");
                startActivityIfNeeded(goMenu, 0);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        listviewMain.setAdapter(new dataListAdapter(IDi, TITLEi, DESCRi, PRIOi, FAITi, DATELi));
        listviewMain.setOnItemClickListener(clicCellule);
    }
    private AdapterView.OnItemClickListener clicCellule = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv =(TextView)view.findViewById(R.id.tache_id);
            String text = tv.getText().toString();
            Intent goAffichage = new Intent(MainActivity.this, AffichageTache.class);
            goAffichage.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            goAffichage.putExtra("IdTache", Integer.parseInt(text));
            startActivity(goAffichage);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    };
    private void RecupererTaches(){
        Cursor data = db.recupMainAffichageTaches();
        IDi = new String[data.getCount()];
        TITLEi = new String[data.getCount()];
        DESCRi = new String[data.getCount()];
        PRIOi = new String[data.getCount()];
        FAITi = new String[data.getCount()];
        DATELi = new String[data.getCount()];
        if (data.moveToFirst()) {
            int i = 0;
            do {
                IDi[i] = String.valueOf(data.getInt(0));
                TITLEi[i] = data.getString(1);
                DESCRi[i] = data.getString(2);
                PRIOi[i] = data.getString(3);
                FAITi[i] = String.valueOf(data.getString(9));
                DATELi[i] = data.getString(6);
                i++;
            } while (data.moveToNext());
        }
    }
    class dataListAdapter extends BaseAdapter {
        String[] ID, Title, Detail, Priorite, Effectue, DateLimite;
        Date date = null;
        dataListAdapter() {
            ID = null;
            Title = null;
            Detail = null;
            Priorite = null;
            Effectue = null;
            DateLimite = null;
        }
        public dataListAdapter(String[] Identifiant, String[] titre, String[] detail, String[] priorite, String[] effectue, String[] datelimite) {
            ID = Identifiant;
            Title = titre;
            Detail = detail;
            Priorite = priorite;
            Effectue = effectue;
            DateLimite = datelimite;
        }
        public int getCount() {
            return Title.length;
        }
        public Object getItem(int arg0) {
            return null;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup Parent) {
            TextView Id, title, detail, categorie, dateLimite;
            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.listview, null);
            Id = (TextView) row.findViewById(R.id.tache_id);
            categorie = (TextView) row.findViewById(R.id.cell_tv_prio);
            title = (TextView) row.findViewById(R.id.cell_tv_titre);
            detail = (TextView) row.findViewById(R.id.cell_tv_descr);
            dateLimite = (TextView) row.findViewById(R.id.cell_tv_date);
            Id.setText(ID[position]);
            title.setText(Title[position]);
            detail.setText(Detail[position]);
            categorie.setText(Priorite[position]);
            if(Priorite[position].equals("Q")) {
                categorie.setTextColor(getResources().getColor(R.color.Quotidien));
                dateLimite.setText("");}
            else{
                dateLimite.setText(GestionDates.ChangerFormatDateSimple(DateLimite[position]));
                if(Priorite[position].equals("R")) {
                    categorie.setTextColor(getResources().getColor(R.color.Recurrent));
                }else if(Priorite[position].equals("CT")) {
                    categorie.setTextColor(getResources().getColor(R.color.CT));
                }else if(Priorite[position].equals("MT")) {
                    categorie.setTextColor(getResources().getColor(R.color.MT));
                }else if(Priorite[position].equals("LT")) {
                    categorie.setTextColor(getResources().getColor(R.color.LT));
                }
            }

            if(FAITi[position].equals("1")){
                title.setTextColor(getResources().getColor(R.color.vert));
                categorie.setTextColor(getResources().getColor(R.color.vert));
                detail.setTextColor(getResources().getColor(R.color.vert));
            }else if(FAITi[position].equals("2")){
                title.setTextColor(getResources().getColor(R.color.rouge));
                categorie.setTextColor(getResources().getColor(R.color.rouge));
                detail.setTextColor(getResources().getColor(R.color.rouge));
            }


            return (row);
        }
    }

}

