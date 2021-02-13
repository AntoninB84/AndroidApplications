package fr.jampa.jampav2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import org.w3c.dom.Text;

public class AffichageTache extends Activity {
    LinearLayout container, lyt_renouv, lyt_dateCreation, lyt_dateRenouv, lyt_btn_effectuer, lyt_rappel;
    SQLiteDataBaseHelper db;
    int IdTache;
    static String IntervalleRecurence = "";
    TextView TitreTache, DescriptionTache, PrioriteTache, CategorieTache, DateCreationTache, DateLimiteTache, DateRenouvellement, TacheEffectue, lyt_sport, RappelTache;
    Button ModifierTache, SupprimerTache, EffectuerTache, RenouvellerTache, PauseTache;

    /**
     * Next version will have english named variables and methods/functions
     * Displayed text will be stored in XML res files to make translations
     * **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affichage_tache);

        container = (LinearLayout)findViewById(R.id.lyt_affichage_tache);
        lyt_sport=(TextView)findViewById(R.id.tv_affichage_titre_tache);
        lyt_renouv =(LinearLayout)findViewById(R.id.lyt_affichage_btn_renouv);
        lyt_dateCreation =(LinearLayout)findViewById(R.id.lyt_affichage_date_creation);
        lyt_dateRenouv =(LinearLayout)findViewById(R.id.lyt_affichage_date_renouv);
        lyt_btn_effectuer =(LinearLayout)findViewById(R.id.lyt_affichage_btn_effectue);
        lyt_rappel = (LinearLayout)findViewById(R.id.tv_affichage_rappel_container);
        TitreTache = (TextView)findViewById(R.id.tv_affichage_titre_tache);
        DescriptionTache = (TextView)findViewById(R.id.tv_affichage_description_tache);
        PrioriteTache = (TextView)findViewById(R.id.tv_affichage_prio_tache);
        CategorieTache = (TextView)findViewById(R.id.tv_affichage_categorie);
        DateCreationTache = (TextView)findViewById(R.id.tv_affichage_date_creation_tache);
        DateRenouvellement = (TextView)findViewById(R.id.tv_affichage_date_renouvellement_tache);
        DateLimiteTache = (TextView)findViewById(R.id.tv_affichage_date_limite_tache);
        TacheEffectue = (TextView)findViewById(R.id.tv_affichage_effectue);
        RappelTache = (TextView)findViewById(R.id.tv_affichage_rappel);
        ModifierTache =(Button)findViewById(R.id.btn_affichage_modifier_tache);
            ModifierTache.setOnClickListener(modififerTache);
        SupprimerTache=(Button)findViewById(R.id.btn_affichage_supprimer_tache);
            SupprimerTache.setOnClickListener(supprimerTache);
        EffectuerTache=(Button)findViewById(R.id.btn_affichage_effectue);
            EffectuerTache.setOnClickListener(effectuerTache);
        RenouvellerTache=(Button)findViewById(R.id.btn_affichage_renouv_now);
            RenouvellerTache.setOnClickListener(renouvellerTache);
        PauseTache=(Button)findViewById(R.id.btn_affichage_pause);
            PauseTache.setOnClickListener(pauseTache);

        Intent RecupId = getIntent();
        IdTache = RecupId.getIntExtra("IdTache", 0);
        db = new SQLiteDataBaseHelper(this);
        Cursor dataQuery = db.recupTacheByID(IdTache);
        while(dataQuery.moveToNext()) {
            TitreTache.setText(dataQuery.getString(1));
            DescriptionTache.setText(dataQuery.getString(2));

            // PRIORITIES
            if (dataQuery.getString(3).equals("R")) {
                int[] intervalle = GestionDates.ConvertFromMillis(dataQuery.getString(7));
                if (intervalle[4] >= 1) {
                    PrioriteTache.setText("Récurrente (Tous les " + intervalle[4] + " an(s))");
                } else if (intervalle[4] < 1 && intervalle[3] >= 1) {
                    PrioriteTache.setText("Récurrente (Tous les " + intervalle[3] + " mois)");
                } else if (intervalle[4] < 1 && intervalle[3] < 1 && intervalle[2] >= 1) {
                    PrioriteTache.setText("Récurrente (Toutes les " + intervalle[2] + " semaine(s))");
                } else if (intervalle[4] < 1 && intervalle[3] < 1 && intervalle[2] < 1 && intervalle[1] >= 1) {
                    PrioriteTache.setText("Récurrente (Tous les " + intervalle[1] + " jour(s))");
                } else if (intervalle[4] < 1 && intervalle[3] < 1 && intervalle[2] < 1 && intervalle[1] < 1) {
                    PrioriteTache.setText("Récurrente (Toutes les " + intervalle[0] + " heures(s))");
                }
            } else if (dataQuery.getString(3).equals("Q")) {
                PrioriteTache.setText("Quotidienne");
                lyt_rappel.setVisibility(View.GONE);
            } else if (dataQuery.getString(3).equals("CT")) {
                PrioriteTache.setText("Court terme");
            } else if (dataQuery.getString(3).equals("MT")) {
                PrioriteTache.setText("Moyen terme");
            } else if (dataQuery.getString(3).equals("LT")) {
                PrioriteTache.setText("Long terme");
            }

            //CATEGORIES
            CategorieTache.setText(dataQuery.getString(12));

            // DATE CREATION ou RENOUVELLEMENT
            if (dataQuery.getString(3).equals("R")) {
                lyt_dateCreation.setVisibility(View.GONE);
                lyt_dateRenouv.setVisibility(View.VISIBLE);
                DateRenouvellement.setText(GestionDates.ChangerFormatDateSimple(dataQuery.getString(5)) + "\n" + GestionDates.RecupHeure(dataQuery.getString(5)));
            } else {
                DateCreationTache.setText(GestionDates.ChangerFormatDateSimple(dataQuery.getString(5)));
            }

            //DATE LIMITE
            if (dataQuery.getString(6).equals("") && dataQuery.getString(3).equals("R")) {
                DateLimiteTache.setText("Tâche en pause");
                PauseTache.setVisibility(View.GONE);
            } else if (dataQuery.getString(3).equals("Q")) {
                DateLimiteTache.setText("Fin de la journée");
            } else {
                DateLimiteTache.setText(GestionDates.ChangerFormatDateSimple(dataQuery.getString(6)) + "\n" + GestionDates.RecupHeure(dataQuery.getString(6)));
            }

            //DONE
            if (dataQuery.getInt(9) == 1 && dataQuery.getString(3).equals("R")) {
                lyt_renouv.setVisibility(View.VISIBLE);
                lyt_btn_effectuer.setVisibility(View.GONE);
                TacheEffectue.setText("Oui ! Bravo !");
            } else if (dataQuery.getInt(9) == 1 && dataQuery.getString(3).equals("Q")) {
                TacheEffectue.setText("Oui ! Bravo !");
                EffectuerTache.setVisibility(View.GONE);
            } else if (dataQuery.getInt(9) == 1) {
                TacheEffectue.setText("Oui ! bravo !");
                ModifierTache.setVisibility(View.GONE);
                EffectuerTache.setVisibility(View.GONE);
            } else if (dataQuery.getInt(9) == 2) {
                TacheEffectue.setText("Non, vous avez raté la date limite !");
                ModifierTache.setVisibility(View.GONE);
                EffectuerTache.setVisibility(View.GONE);
            } else {
                TacheEffectue.setText("Pas encore !");
            }

            IntervalleRecurence = dataQuery.getString(7);


            //REMINDER
            if (dataQuery.getInt(10) == 1) {
                RappelTache.setText("Le rappel est déjà passé !");
            } else {
                if (dataQuery.getString(8).equals("")) {
                    RappelTache.setText("Aucun rappel programmé !");
                } else {
                    int[] rappel = GestionDates.ConvertFromMillis(dataQuery.getString(8));
                    String dateRappel = GestionDates.CalculerDateRappel(dataQuery.getString(6), dataQuery.getString(8));
                    if (rappel[4] >= 1) {
                        RappelTache.setText(rappel[4] + " an(s) avant\n" + dateRappel);
                    } else if (rappel[4] == 0 && rappel[3] >= 1) {
                        RappelTache.setText(rappel[3] + " mois avant\n" + dateRappel);
                    } else if (rappel[4] == 0 && rappel[3] == 0 && rappel[2] >= 1) {
                        RappelTache.setText(rappel[2] + " semaine(s) avant\n" + dateRappel);
                    } else if (rappel[4] == 0 && rappel[3] == 0 && rappel[2] == 0 && rappel[1] >= 1) {
                        RappelTache.setText(rappel[1] + " jour(s) avant\n" + dateRappel);
                    } else {
                        RappelTache.setText(rappel[0] + " heure(s) avant\n" + dateRappel);
                    }
                }
            }
        }

        if(CategorieTache.getText().equals("Sport")){
            lyt_sport.setOnLongClickListener(ActiviteSport);
        }

    }
    @Override
    public void onBackPressed(){
        Intent goMain = new Intent(AffichageTache.this, MainActivity.class);
        startActivity(goMain);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    private View.OnClickListener modififerTache = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent goCreation = new Intent(AffichageTache.this, CreationTache.class);
            goCreation.putExtra("Action", "modification");
            goCreation.putExtra("IdTache", IdTache);
            startActivity(goCreation);
            finish();
        }
    };
    private View.OnClickListener supprimerTache = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(AffichageTache.this);
            dialog.setTitle("Alerte suppression !");
            dialog.setMessage("Souhaitez-vous réellement supprimer cette tâche ?");
            dialog.setCancelable(true);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    db.supprimerTache(IdTache);
                    Intent goAccueil = new Intent(AffichageTache.this, MainActivity.class);
                    startActivity(goAccueil);
                    finish();
                }});
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }});
            AlertDialog alert = dialog.create();
            alert.show();

        }
    };
    private View.OnClickListener effectuerTache = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            db.setEffectue(IdTache);
            reload();
        }
    };
    private View.OnClickListener renouvellerTache = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String maintenant = GestionDates.DateActuelle();
            String nouvelleLimite = GestionDates.CalculerDateLimite(maintenant, IntervalleRecurence);
            db.setRenouvelle(IdTache, nouvelleLimite, maintenant);
            reload();
        }
    };
    private View.OnClickListener pauseTache = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            db.setPause(IdTache);
            reload();
        }
    };
    private View.OnLongClickListener ActiviteSport = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Intent goSport = new Intent(AffichageTache.this, ActiviteSportive.class);
            goSport.putExtra("description", DescriptionTache.getText());
            startActivityIfNeeded(goSport, 0);
            return false;
        }
    };
    public void reload(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
