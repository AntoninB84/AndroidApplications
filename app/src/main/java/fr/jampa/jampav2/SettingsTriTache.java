package fr.jampa.jampav2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingsTriTache extends Activity {
    CheckBox cb_alpha, cb_categorie, cb_q, cb_r, cb_ct, cb_mt, cb_lt, cb_effectue, cb_rate;
    RadioGroup rg_date;
    Spinner sp_categorie, sp_mois, sp_annee;
    String tempPrio;
    SQLiteDataBaseHelper db;
    ScrollView container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_tri_tache);
        db = new SQLiteDataBaseHelper(this);

        container=(ScrollView)findViewById(R.id.container_settings);
            container.setOnTouchListener(new OnSwipeTouchListener(SettingsTriTache.this){
                public void onSwipeRight(){
                    onBackPressed();
                }
            });
        cb_alpha=(CheckBox)findViewById(R.id.cb_alpha);
        cb_categorie=(CheckBox)findViewById(R.id.cb_tri_categorie);
        sp_categorie=(Spinner)findViewById(R.id.sp_tri_categorie);
        initListCategories();
        cb_q=(CheckBox)findViewById(R.id.cb_tri_q);
        cb_r=(CheckBox)findViewById(R.id.cb_tri_r);
        cb_ct=(CheckBox)findViewById(R.id.cb_tri_ct);
        cb_mt=(CheckBox)findViewById(R.id.cb_tri_mt);
        cb_lt=(CheckBox)findViewById(R.id.cb_tri_lt);
        rg_date=(RadioGroup)findViewById(R.id.rg_tri_date);
        sp_mois=(Spinner)findViewById(R.id.sp_tri_mois);
        sp_annee=(Spinner)findViewById(R.id.sp_tri_annee);
        initSpinners();
        cb_effectue=(CheckBox)findViewById(R.id.cb_tri_effectue);
        cb_rate=(CheckBox)findViewById(R.id.cb_tri_rate);


        //Affichage en fonction de la BDD
        Cursor settings = db.recupSettings();
        settings.moveToFirst();
            if(settings.getInt(1)==1){cb_alpha.setChecked(true);}else{cb_alpha.setChecked(false);}
        int j = 0;
        for(int i = 0; i < sp_categorie.getCount(); i++){
            if(String.valueOf(sp_categorie.getItemIdAtPosition(i)).equals(String.valueOf(settings.getInt(2)))){
                j=i;
                break;
            }
        }
        sp_categorie.setSelection(j);
            if(settings.getInt(3)==1){cb_q.setChecked(true);}else{cb_q.setChecked(false);}
            if(settings.getInt(4)==1){cb_r.setChecked(true);}else{cb_r.setChecked(false);}
            if(settings.getInt(5)==1){cb_ct.setChecked(true);}else{cb_ct.setChecked(false);}
            if(settings.getInt(6)==1){cb_mt.setChecked(true);}else{cb_mt.setChecked(false);}
            if(settings.getInt(7)==1){cb_lt.setChecked(true);}else{cb_lt.setChecked(false);}
            if(settings.getInt(8)==1){cb_effectue.setChecked(true);}else{cb_effectue.setChecked(false);}
            if(settings.getInt(9)==1){cb_rate.setChecked(true);}else{cb_rate.setChecked(false);}
            if(settings.getInt(10)==-1){rg_date.clearCheck();}else if(settings.getInt(10)==0){rg_date.check(R.id.rb_date_creation);}
            else if(settings.getInt(10)==1){rg_date.check(R.id.rb_date_limite);}else if(settings.getInt(10)==2){rg_date.check(R.id.rb_date_limite_DESC);}
            else if(settings.getInt(10)==3){rg_date.check(R.id.rb_date_limite_sup);}else if(settings.getInt(10)==4){rg_date.check(R.id.rb_date_limite_inf);}
            else if(settings.getInt(10)==5){rg_date.check(R.id.rb_date_limite_egale);}

    }
    public void initListCategories(){
        Cursor categories = db.recupCategories();
        SimpleCursorAdapter adapterr = new SimpleCursorAdapter (this, android.R.layout.simple_spinner_item, categories, new String[]{"cat"}, new int[]{android.R.id.text1});
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_categorie.setAdapter(adapterr);
    }
    public void initSpinners(){
        /**
         *
         * There was definitely a better way to do this...
         *
         * **/
        List<String> spinnerIntervalle = new ArrayList<String>();
        spinnerIntervalle.add("Janvier");
        spinnerIntervalle.add("Février");
        spinnerIntervalle.add("Mars");
        spinnerIntervalle.add("Avril");
        spinnerIntervalle.add("Mai");
        spinnerIntervalle.add("Juin");
        spinnerIntervalle.add("Juillet");
        spinnerIntervalle.add("Août");
        spinnerIntervalle.add("Septembre");
        spinnerIntervalle.add("Octobre");
        spinnerIntervalle.add("Novembre");
        spinnerIntervalle.add("Décembre");
        ArrayAdapter<String> IntervalleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerIntervalle);
        IntervalleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_mois.setAdapter(IntervalleAdapter);
        List<String> spinnerAnnee = new ArrayList<String>();
        spinnerAnnee.add("2020");        spinnerAnnee.add("2021");        spinnerAnnee.add("2022");        spinnerAnnee.add("2023");
        spinnerAnnee.add("2024");        spinnerAnnee.add("2025");        spinnerAnnee.add("2026");        spinnerAnnee.add("2027");
        spinnerAnnee.add("2028");        spinnerAnnee.add("2029");        spinnerAnnee.add("2030");        spinnerAnnee.add("2031");
        spinnerAnnee.add("2032");        spinnerAnnee.add("2033");        spinnerAnnee.add("2034");        spinnerAnnee.add("2035");
        spinnerAnnee.add("2036");        spinnerAnnee.add("2037");        spinnerAnnee.add("2038");        spinnerAnnee.add("2039");
        spinnerAnnee.add("2040");        spinnerAnnee.add("2041");        spinnerAnnee.add("2042");        spinnerAnnee.add("2043");
        ArrayAdapter<String> AnneeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerAnnee);
        AnneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_annee.setAdapter(AnneeAdapter);
    }
    @Override
    public void onBackPressed(){
        int alpha, categorieTemp, Q, Rc, CT, MT, LT, rate, effectue, date, mois, annee, verif;
        String ref;
        if(cb_alpha.isChecked()==true){alpha = 1;}else{alpha=0;}
        if(cb_categorie.isChecked()==true){
            categorieTemp = Math.toIntExact(sp_categorie.getSelectedItemId());
        }else{categorieTemp = 0;}
        if(cb_q.isChecked()==true){Q=1;}else{Q=0;}
        if(cb_r.isChecked()==true){Rc=1;}else{Rc=0;}
        if(cb_ct.isChecked()==true){CT=1;}else{CT=0;}
        if(cb_mt.isChecked()==true){MT=1;}else{MT=0;}
        if(cb_lt.isChecked()==true){LT=1;}else{LT=0;}
        if(cb_rate.isChecked()==true){rate=1;}else{rate=0;}
        if(cb_effectue.isChecked()==true){effectue=1;}else{effectue=0;}
        date = -1;
        if(rg_date.getCheckedRadioButtonId()==-1){date = -1;}
        else if(rg_date.getCheckedRadioButtonId()==R.id.rb_date_creation){date = 0;}
        else if(rg_date.getCheckedRadioButtonId()==R.id.rb_date_limite){date = 1;}
        else if(rg_date.getCheckedRadioButtonId()==R.id.rb_date_limite_DESC){date = 2;}
        else if(rg_date.getCheckedRadioButtonId()==R.id.rb_date_limite_sup){date = 3;}
        else if(rg_date.getCheckedRadioButtonId()==R.id.rb_date_limite_inf){date = 4;}
        else if(rg_date.getCheckedRadioButtonId()==R.id.rb_date_limite_egale){date = 5;}
        ref = sp_annee.getSelectedItem().toString()+"-"; verif = 0;
        if(sp_mois.getSelectedItemPosition()<9){verif = 1;}
        if(verif == 1){ ref = ref +"0"+ (sp_mois.getSelectedItemPosition()+1)+"-01 00:00:00";}
        else{ref = ref + (sp_mois.getSelectedItemPosition()+1)+"-01 00:00:00";}

        db.updateSettings(alpha, categorieTemp, Q, Rc, CT, MT, LT, effectue, rate, date, ref);
        Intent goMenu = new Intent(SettingsTriTache.this, MainActivity.class);
        startActivityIfNeeded(goMenu, 0);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
