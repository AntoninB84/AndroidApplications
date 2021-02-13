package fr.jampa.jampav2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CreationTache extends Activity {
    LinearLayout container_creation, container_categorie, creation_intervalle, creation_rappel, lyt_dateDepart, lyt_dateLimite;
    EditText titre, description, delai_rappel, duree_intervalle, titreCategorie;
    TextView btn_onglet_creation, btn_onglet_categorie;
    RadioGroup rg_priorite;
    Spinner choix_categorie, sp_delai_rappel, sp_duree_intervalle, sp_liste_categorie;
    CalendarView dateLimite, dateDepart;
    Button creer, ajouterCategorie, supprimerCategorie;
    RadioButton rb_ct, rb_q, rb_r, rb_mt, rb_lt;
    CheckBox cb_dateLimite;
    SQLiteDataBaseHelper db;
    String dateDepartchoisie, action;
    Intent RecupAction;
    int IDtache, categorie;
    static int anneeC, moisC, jourC, heureC, minuteC, anneeL, moisL, jourL, heureL, minuteL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_tache);
        db = new SQLiteDataBaseHelper(this);
        container_creation=(LinearLayout)findViewById(R.id.lyt_creation_tache);
        container_categorie=(LinearLayout)findViewById(R.id.lyt_creation_categorie);
        btn_onglet_creation =(TextView)findViewById(R.id.tv_btn_creation);
            btn_onglet_creation.setOnClickListener(afficherCreation);
        btn_onglet_categorie =(TextView)findViewById(R.id.tv_btn_categorie);
            btn_onglet_categorie.setOnClickListener(afficherCategorie);

        sp_liste_categorie=(Spinner)findViewById(R.id.sp_liste_categorie);
        ajouterCategorie=(Button)findViewById(R.id.btn_ajouter_categorie);
            ajouterCategorie.setOnClickListener(creerCategorie);
        supprimerCategorie=(Button)findViewById(R.id.btn_supprimer_categorie);
            supprimerCategorie.setOnClickListener(deleteCategorie);
        titreCategorie=(EditText)findViewById(R.id.et_titre_categorie);

        titre =(EditText)findViewById(R.id.et_titre_tache);
        description=(EditText)findViewById(R.id.et_description_tache);

        rg_priorite=(RadioGroup)findViewById(R.id.rg_priorite);
            rg_priorite.setOnCheckedChangeListener(Priorite);
         rb_q=(RadioButton)findViewById(R.id.rb_q);
         rb_r=(RadioButton)findViewById(R.id.rb_r);
        rb_ct=(RadioButton)findViewById(R.id.rb_ct);
        rb_mt=(RadioButton)findViewById(R.id.rb_mt);
        rb_lt=(RadioButton)findViewById(R.id.rb_lt);

        choix_categorie=(Spinner)findViewById(R.id.sp_choix_categorie);

        creation_intervalle=(LinearLayout)findViewById(R.id.lyt_creation_intervalle);
        duree_intervalle=(EditText)findViewById(R.id.et_intervalle_number);
        sp_duree_intervalle=(Spinner)findViewById(R.id.sp_creation_intervalle);

        lyt_dateDepart=(LinearLayout)findViewById(R.id.lyt_dateDepart);
        dateDepart=(CalendarView)findViewById(R.id.cw_creation_datedepart);
        dateDepart.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreationTache.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutee) {
                        heureC = hourOfDay;
                        minuteC = minutee;
                    }
                    }, 12, 00, true);timePickerDialog.show();
                anneeC = year; moisC = month; jourC = dayOfMonth;
            }
        });

        lyt_dateLimite=(LinearLayout)findViewById(R.id.lyt_date_limite);
        cb_dateLimite=(CheckBox)findViewById(R.id.cb_date_limite);
        dateLimite=(CalendarView)findViewById(R.id.cw_creation_datelimite);
        dateLimite.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreationTache.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minutee) {
                        heureL = hourOfDay;
                        minuteL = minutee;
                    }
                }, 12, 00, true);timePickerDialog.show();
               anneeL = year; moisL = month; jourL = dayOfMonth;
            }
        });

        creation_rappel=(LinearLayout)findViewById(R.id.lyt_creation_rappel);
        delai_rappel=(EditText)findViewById(R.id.et_rappel_number);
        sp_delai_rappel=(Spinner)findViewById(R.id.sp_creation_rappel);

            cb_dateLimite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(cb_dateLimite.isChecked()==true){
                        dateLimite.setVisibility(View.VISIBLE);
                        creation_rappel.setVisibility(View.VISIBLE);
                    }else{
                        dateLimite.setVisibility(View.GONE);
                        creation_rappel.setVisibility(View.GONE);
                        delai_rappel.setText("");
                    }
                }
            });

        creer = (Button)findViewById(R.id.btn_creation_tache);
        creer.setOnClickListener(ajouterTache);

        rb_ct.setChecked(true);

        RecupAction = getIntent();
        action = RecupAction.getStringExtra("Action");
        if(action.equals("modification")){
            Cursor TacheModif = db.recupTacheByID(RecupAction.getIntExtra("IdTache", 0));
            while(TacheModif.moveToNext()){
                IDtache=TacheModif.getInt(0);
                titre.setText(TacheModif.getString(1));
                description.setText(TacheModif.getString(2));
                categorie = TacheModif.getInt(4);
                Log.i("test1", String.valueOf(categorie));
                String prio = TacheModif.getString(3);
                if(prio.equals("Q")){ rb_q.setChecked(true); }else if(prio.equals("R")){rb_r.setChecked(true);}
                else if(prio.equals("CT")){ rb_ct.setChecked(true); }else if(prio.equals("MT")){ rb_mt.setChecked(true); }else if(prio.equals("LT")){ rb_lt.setChecked(true); }
                if(TacheModif.getString(6).equals("")){}else{dateLimite.setDate(GestionDates.TransformerDateEnLong(GestionDates.TransformerEnDate(TacheModif.getString(6))));cb_dateLimite.setChecked(true);}
                creer.setText("Modifier");
            }

        }
        initListCategories();
        initRappel();
        initIntervalle();
    }

    public RadioGroup.OnCheckedChangeListener Priorite = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.rb_q){
                creation_intervalle.setVisibility(View.GONE);
                lyt_dateDepart.setVisibility(View.GONE);
                lyt_dateLimite.setVisibility(View.GONE);
                creation_rappel.setVisibility(View.GONE);
            }else if(checkedId == R.id.rb_r){
                creation_intervalle.setVisibility(View.VISIBLE);
                lyt_dateDepart.setVisibility(View.VISIBLE);
                dateDepart.setVisibility(View.VISIBLE);
                lyt_dateLimite.setVisibility(View.GONE);
                creation_rappel.setVisibility(View.VISIBLE);
            }else if(checkedId == R.id.rb_ct || checkedId == R.id.rb_mt || checkedId == R.id.rb_lt ){
                creation_intervalle.setVisibility(View.GONE);
                lyt_dateDepart.setVisibility(View.GONE);
                dateDepart.setVisibility(View.GONE);
                lyt_dateLimite.setVisibility(View.VISIBLE);
                dateLimite.setVisibility(View.GONE);
                creation_rappel.setVisibility(View.GONE);
            }
        }
    };
    public void initListCategories(){
       Cursor categories = db.recupCategories();
       SimpleCursorAdapter adapterr = new SimpleCursorAdapter (this, android.R.layout.simple_spinner_item, categories, new String[]{"cat"}, new int[]{android.R.id.text1});
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choix_categorie.setAdapter(adapterr);
        RecupAction = getIntent();
        action = RecupAction.getStringExtra("Action");
        if(action.equals("modification")) {
            Log.i("test", String.valueOf(categorie));
           int pos = 0;
           for(int i = 0; i<adapterr.getCount(); i++){
               categories.moveToPosition(i);
               int temp = categories.getInt(0);
               if(temp == categorie){
                   pos = i;
                   break;
               }
           }
            choix_categorie.setSelection(pos);
        }
        sp_liste_categorie.setAdapter(adapterr);
    }
    public void initRappel(){
        List<String> spinnerRappel = new ArrayList<String>();
        spinnerRappel.add("Heure(s) avant");
        spinnerRappel.add("Jour(s) avant");
        spinnerRappel.add("Semaine(s) avant");
        spinnerRappel.add("Mois avant");
        spinnerRappel.add("Annee(s) avant");
        ArrayAdapter<String> RappelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerRappel);
        RappelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_delai_rappel.setAdapter(RappelAdapter);
    }
    public void initIntervalle(){
        List<String> spinnerIntervalle = new ArrayList<String>();
        spinnerIntervalle.add("Jour(s)");
        spinnerIntervalle.add("Semaine(s)");
        spinnerIntervalle.add("Mois");
        spinnerIntervalle.add("Annee(s)");
        ArrayAdapter<String> IntervalleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerIntervalle);
        IntervalleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_duree_intervalle.setAdapter(IntervalleAdapter);
    }
    public View.OnClickListener creerCategorie = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String titre = titreCategorie.getText().toString();
            if(titre.equals("")){
                alerte("Attention !", "Il faut donner un nom à la catégorie !");
            }else{
                db.insererCategorie(titre);
                goAccueil();
            }
        }
    };
    public View.OnClickListener deleteCategorie = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(sp_liste_categorie.getSelectedItemId() != 1){
                db.supprimerCategorie(Math.toIntExact(sp_liste_categorie.getSelectedItemId()));
                reload();
            }else{
                alerte("Attention !", "Vous ne pouvez pas supprimer cette catégorie !");
            }
        }
    };
    public View.OnClickListener afficherCreation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            container_creation.setVisibility(View.VISIBLE);
            btn_onglet_creation.setBackgroundResource(R.drawable.border_bottom_large);
            container_categorie.setVisibility(View.GONE);
            btn_onglet_categorie.setBackgroundResource(R.drawable.border_bottom);
        }
    };
    public View.OnClickListener afficherCategorie = new View.OnClickListener(){
      @Override
      public void onClick(View v){
          container_creation.setVisibility(View.GONE);
          btn_onglet_creation.setBackgroundResource(R.drawable.border_bottom);
          container_categorie.setVisibility(View.VISIBLE);
          btn_onglet_categorie.setBackgroundResource(R.drawable.border_bottom_large);
      }
    };
    public View.OnClickListener ajouterTache = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String titreTemp, descrTemp, prioriteTemp, intervalleTemp, rappelTemp, dateCreationTemp, dateLimiteTemp;
            int categorieTemp;
            titreTemp = titre.getText().toString();
            descrTemp = description.getText().toString();
            categorieTemp = Math.toIntExact(choix_categorie.getSelectedItemId());
            intervalleTemp = duree_intervalle.getText().toString();
            dateCreationTemp = String.valueOf(anneeC)+"-"+String.valueOf(moisC+1)+"-"+String.valueOf(jourC)+" "+String.valueOf(heureC)+":"+String.valueOf(minuteC)+":00";
            dateLimiteTemp = String.valueOf(anneeL)+"-"+String.valueOf(moisL+1)+"-"+String.valueOf(jourL)+" "+String.valueOf(heureL)+":"+String.valueOf(minuteL)+":00";

            rappelTemp = delai_rappel.getText().toString();

            if(titreTemp.equals("") || descrTemp.equals("")) {
                alerte("Attention !", "Il est nécessaire de donner un titre et une description à votre tâche !");
            }else{
                if (rappelTemp.equals("")) {
                } else {
                    if (sp_delai_rappel.getSelectedItemPosition() == 0) {
                        long calculrappel = Long.parseLong(rappelTemp);
                        rappelTemp = String.valueOf(calculrappel * 3600000L);
                    } else if (sp_delai_rappel.getSelectedItemPosition() == 1) {
                        long calculrappel = Long.parseLong(rappelTemp);
                        rappelTemp = String.valueOf(calculrappel * 86400000L);
                    } else if (sp_delai_rappel.getSelectedItemPosition() == 2) {
                        long calculrappel = Long.parseLong(rappelTemp);
                        rappelTemp = String.valueOf(calculrappel * 604800000L);
                    } else if (sp_delai_rappel.getSelectedItemPosition() == 3) {
                        long calculrappel = Long.parseLong(rappelTemp);
                        rappelTemp = String.valueOf(calculrappel * 2592000000L);
                    } else if (sp_delai_rappel.getSelectedItemPosition() == 4) {
                        long calculrappel = Long.parseLong(rappelTemp);
                        rappelTemp = String.valueOf(calculrappel * 31536000000L);
                    }
                }

                if (rg_priorite.getCheckedRadioButtonId() == R.id.rb_q) {
                    rappelTemp = "";
                    intervalleTemp = "";
                    prioriteTemp = "Q";
                    if(action.equals("modification")){
                        db.updateTache(IDtache, titreTemp, descrTemp, prioriteTemp, categorieTemp, GestionDates.DateActuelle(), "", intervalleTemp, rappelTemp);
                    }else {
                        db.insererTache(titreTemp, descrTemp, prioriteTemp, categorieTemp, GestionDates.DateActuelle(), "", intervalleTemp, rappelTemp, 0);
                    }
                    goAccueil();
                } else if (rg_priorite.getCheckedRadioButtonId() == R.id.rb_r) {
                    prioriteTemp = "R";
                    if (intervalleTemp.equals("")) {
                        alerte("Attention !", "Il est nécessaire d'insérer une intervalle de récurrence !");
                    } else {
                        if (sp_duree_intervalle.getSelectedItemPosition() == 0) {
                            long calculIntervalle = Long.parseLong(intervalleTemp);
                            intervalleTemp = String.valueOf(calculIntervalle * 86400000L);
                        } else if (sp_duree_intervalle.getSelectedItemPosition() == 1) {
                            long calculIntervalle = Long.parseLong(intervalleTemp);
                            intervalleTemp = String.valueOf(calculIntervalle * 604800000L);
                        } else if (sp_duree_intervalle.getSelectedItemPosition() == 2) {
                            long calculIntervalle = Long.parseLong(intervalleTemp);
                            intervalleTemp = String.valueOf(calculIntervalle * 2592000000L);
                        } else if (sp_duree_intervalle.getSelectedItemPosition() == 3) {
                            long calculIntervalle = Long.parseLong(intervalleTemp);
                            intervalleTemp = String.valueOf(calculIntervalle * 31536000000L);
                        }
                        if(dateCreationTemp != null && !dateCreationTemp.isEmpty()){
                            dateLimiteTemp = GestionDates.CalculerDateLimite(dateCreationTemp, intervalleTemp);
                            Log.i("Test", dateLimiteTemp);
                            if (action.equals("modification")) {
                                db.updateTache(IDtache, titreTemp, descrTemp, prioriteTemp, categorieTemp, dateCreationTemp, dateLimiteTemp, intervalleTemp, rappelTemp);
                            } else {
                                db.insererTache(titreTemp, descrTemp, prioriteTemp, categorieTemp, dateCreationTemp, dateLimiteTemp, intervalleTemp, rappelTemp, 0);
                            }
                            goAccueil();
                        }else {
                            alerte("Attention !", "Il est nécessaire de sélectionner un départ pour une tâche récurrente ! ( Vous pouvez très bien sélectionner la date actuelle)");
                        }
                    }
                } else if (rg_priorite.getCheckedRadioButtonId() == R.id.rb_ct) {
                    prioriteTemp = "CT";
                    if (cb_dateLimite.isChecked() == false) {
                        dateLimiteTemp = "";
                    }
                    if(action.equals("modification")){
                        db.updateTache(IDtache, titreTemp, descrTemp, prioriteTemp, categorieTemp, GestionDates.DateActuelle(), dateLimiteTemp, "", rappelTemp);
                    }else {
                        db.insererTache(titreTemp, descrTemp, prioriteTemp, categorieTemp, GestionDates.DateActuelle(), dateLimiteTemp, "", rappelTemp, 0);
                    }
                    goAccueil();
                } else if (rg_priorite.getCheckedRadioButtonId() == R.id.rb_mt) {
                    prioriteTemp = "MT";
                    if (cb_dateLimite.isChecked() == false) {
                        dateLimiteTemp = "";
                    }if(action.equals("modification")){
                        db.updateTache(IDtache, titreTemp, descrTemp, prioriteTemp, categorieTemp, GestionDates.DateActuelle(), dateLimiteTemp, "", rappelTemp);
                    }else {
                        db.insererTache(titreTemp, descrTemp, prioriteTemp, categorieTemp, GestionDates.DateActuelle(), dateLimiteTemp, "", rappelTemp, 0);
                    }
                    goAccueil();
                } else if (rg_priorite.getCheckedRadioButtonId() == R.id.rb_lt) {
                    prioriteTemp = "LT";
                    if (cb_dateLimite.isChecked() == false) {
                        dateLimiteTemp = "";
                    }
                    if(action.equals("modification")){
                        db.updateTache(IDtache, titreTemp, descrTemp, prioriteTemp, categorieTemp, GestionDates.DateActuelle(), dateLimiteTemp, "", rappelTemp);
                    }else {
                        db.insererTache(titreTemp, descrTemp, prioriteTemp, categorieTemp, GestionDates.DateActuelle(), dateLimiteTemp, "", rappelTemp, 0);
                    }
                    goAccueil();
                }
            }
        }
    };
    @Override
    public void onBackPressed(){
        goAccueil();
    }
    public void reload(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    private void goAccueil(){
        Intent goMain = new Intent(CreationTache.this, MainActivity.class);
        startActivity(goMain);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    private void alerte(String titre, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(CreationTache.this);
        dialog.setTitle(titre);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }});
        AlertDialog alert = dialog.create();
        alert.show();
    }
}
