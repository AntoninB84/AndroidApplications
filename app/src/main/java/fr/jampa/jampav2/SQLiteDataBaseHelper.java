package fr.jampa.jampav2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

    public static final String NOM_BDD = "JampaBDD.db";
    public static final String TACHE_NOM_TABLE = "taches";
    public static final String CATEGORIE_NOM_TABLE = "categories";
    public static final String USER_NOM_TABLE = "preferences";


    public SQLiteDataBaseHelper(Context context) {
        super(context, NOM_BDD, null, 17);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TACHE_NOM_TABLE + "( " +
                "IDTACHE INTEGER PRIMARY KEY AUTOINCREMENT, " +     // 0 Id
                "TITRE TEXT, " +                                    // 1 titre
                "DESCRIPTION TEXT, " +                              // 2 descr
                "PRIORITE TEXT, " +                                 // 3 prio
                "CATEGORIE INT, " +                                 // 4 categ
                "DATECREATION TEXT, " +                             // 5 dateCrea
                "DATELIMITE TEXT, " +                               // 6 dateLim
                "INTERVALLEREC TEXT, " +                            // 7 intervalleRec
                "RAPPELTACHE TEXT, " +                              // 8 RappelTache
                "EFFECTUE INTEGER, " +                              // 9 effectue
                "NOTIF INTEGER)");                                  // 10 Notif
        db.execSQL("CREATE TABLE " + CATEGORIE_NOM_TABLE + "(IDCAT INTEGER PRIMARY KEY AUTOINCREMENT, NOMCATEGORIE TEXT)");
        db.execSQL("CREATE TABLE " + USER_NOM_TABLE + "(" +
                "IDUSER INTEGER PRIMARY KEY, " +    // 0
                "TRIALPHA INTEGER, " +              // 1
                "TRICATEGORIE INTEGER, " +          // 2
                "TRIQ INTEGER, " +                  // 3
                "TRIR INTEGER, " +                  // 4
                "TRICT INTEGER, " +                 // 5
                "TRIMT INTEGER, " +                 // 6
                "TRILT INTEGER, " +                 // 7
                "TRIEFFECTUE INTEGER, " +           // 8
                "TRIRATE INTEGER, " +               // 9
                "TRIDATE INTEGER, " +               // 10
                "DATEREFERENCE TEXT, " +            // 11
                "JAMPOINTS INTEGER)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("IDUSER", 1); contentValues.put("TRIALPHA", 0); contentValues.put("TRICATEGORIE", 0);contentValues.put("TRIQ", 1); contentValues.put("TRIR", 1);
        contentValues.put("TRICT", 1); contentValues.put("TRIMT", 1); contentValues.put("TRILT", 1); contentValues.put("TRIEFFECTUE", 0); contentValues.put("TRIRATE", 0);
        contentValues.put("TRIDATE", 1); contentValues.put("DATEREFERENCE", "2020-06-10 15:00:00"); contentValues.put("JAMPOINTS", 0);
        db.insert(USER_NOM_TABLE, null, contentValues);
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("NOMCATEGORIE", "Autres");
        db.insert(CATEGORIE_NOM_TABLE, null, contentValues2);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO SUPPRIMER LES TABLES POUR REPARTIR AVEC DES BONNES BASES
        //db.execSQL("DROP TABLE IF EXISTS " + TACHE_NOM_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + CATEGORIE_NOM_TABLE);
        //db.execSQL("ALTER TABLE taches ADD COLUMN NOTIF INTEGER DEFAULT 0;"); ////////////////////////////////////////////////////////////////////////
        //onCreate(db);
    }
    public Cursor recupSettings(){
        SQLiteDatabase db = this.getWritableDatabase();
        String requete = "select * from "+USER_NOM_TABLE;
        Cursor result = db.rawQuery(requete, null);
        return result;
    }
    public void updateSettings(int alpha, int categorie, int Q, int Rc, int CT, int MT, int LT, int effectue, int rate, int date, String ref){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TRIALPHA", alpha);
        contentValues.put("TRICATEGORIE", categorie);
        contentValues.put("TRIQ", Q);
        contentValues.put("TRIR", Rc);
        contentValues.put("TRICT", CT);
        contentValues.put("TRIMT", MT);
        contentValues.put("TRILT", LT);
        contentValues.put("TRIEFFECTUE", effectue);
        contentValues.put("TRIRATE", rate);
        contentValues.put("TRIDATE", date);
        contentValues.put("DATEREFERENCE", ref);
        db.update(USER_NOM_TABLE, contentValues, "IDUSER=1", null);
        db.close();
    }
    public void insererTache(String titre, String description, String priorite, int categorie, String dateCreation, String dateLimite, String intervalle, String rappel, int effectue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITRE", titre);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("PRIORITE", priorite);
        contentValues.put("CATEGORIE", categorie);
        contentValues.put("DATECREATION", dateCreation);
        contentValues.put("DATELIMITE", dateLimite);
        contentValues.put("INTERVALLEREC", intervalle);
        contentValues.put("RAPPELTACHE", rappel);
        contentValues.put("EFFECTUE", effectue);
        contentValues.put("NOTIF", 0);
        db.insert(TACHE_NOM_TABLE, null, contentValues);
        db.close();
    }
    public void updateTache(int id, String titre, String description, String priorite, int categorie, String dateCreation, String dateLimite, String intervalle, String rappel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITRE", titre);
        contentValues.put("DESCRIPTION", description);
        contentValues.put("PRIORITE", priorite);
        contentValues.put("CATEGORIE", categorie);
        contentValues.put("DATECREATION", dateCreation);
        contentValues.put("DATELIMITE", dateLimite);
        contentValues.put("INTERVALLEREC", intervalle);
        contentValues.put("RAPPELTACHE", rappel);
        contentValues.put("NOTIF", 0);
        db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE="+id, null);
        db.close();
    }
    public void insererCategorie(String nom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOMCATEGORIE", nom);
        db.insert(CATEGORIE_NOM_TABLE, null, contentValues);
        db.close();
    }
    public void supprimerCategorie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CATEGORIE_NOM_TABLE, "IDCAT" + " = " + id, null);
        String requete = "select * from "+TACHE_NOM_TABLE+" where CATEGORIE = "+id;
        Cursor result = db.rawQuery(requete, null);
        if(result.moveToFirst()){
            int i = 0;
            int[] idTache = new int[result.getCount()];
            do{
                idTache[i] = result.getInt(0);
                ContentValues contentValues = new ContentValues();
                contentValues.put("CATEGORIE", 1);
                db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE="+idTache, null);
            }while(result.moveToNext());
        }
        db.close();
    }
    public Cursor recupCategories(){
        SQLiteDatabase db = this.getWritableDatabase();
        String requete = "select IDCAT as _id, NOMCATEGORIE as cat from "+CATEGORIE_NOM_TABLE;
        Cursor result = db.rawQuery(requete, null);
        return result;
    }
    public Cursor recupTachesNotif(String dateActuelle){
        Log.i("Steps", "On est passé par RecupTachesNotif");
        SQLiteDatabase db = this.getWritableDatabase();
        String requete ="select * from "+TACHE_NOM_TABLE+" where DATELIMITE != '' and RAPPELTACHE != '' and DATELIMITE >= datetime('"+dateActuelle+"') AND NOTIF = 0";
        Cursor result = db.rawQuery(requete, null);
        return result;
    }
    public Cursor recupMainAffichageTaches(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor user = db.rawQuery("select * from "+USER_NOM_TABLE, null);
        user.moveToFirst();
        String requete = "select * from " + TACHE_NOM_TABLE + " WHERE NOT EFFECTUE = 1 AND NOT EFFECTUE = 2";
        int compteur = 0;
        int compteur2 = 0;
        int compteurBy =0;
        int newCompteur =0;

        if((user.getInt(8) == 1)&&(user.getInt(9)==0)){
            requete = "select * from "+TACHE_NOM_TABLE+" WHERE EFFECTUE in (0, 1)"; // NOT EFFECTUE = 2 AND EFFECTUE = 1";
        newCompteur++;}
        if((user.getInt(8) == 0)&&(user.getInt(9)==1)){
            requete = "select * from "+TACHE_NOM_TABLE+" WHERE EFFECTUE in (0, 2)"; // NOT EFFECTUE = 1 AND EFFECTUE = 2";
        newCompteur++;}
        if((user.getInt(8) == 1)&&(user.getInt(9)==1)){
            requete = "select * from "+TACHE_NOM_TABLE+" WHERE EFFECTUE in (0, 1, 2)";
            compteur++; }

        if(user.getInt(2) != 0){
            requete = requete + " and CATEGORIE = "+user.getInt(2);
            compteur++; }

        if(user.getInt(3) == 1){
            requete = requete + " and PRIORITE in ('Q',";
            compteur++; compteur2++;}
        if(user.getInt(4) == 1) {
            if (compteur2 == 0) { requete = requete + " and PRIORITE in ('R',"; compteur++; compteur2++; }
            else if (compteur2 >= 1) { requete = requete + "'R',";
                compteur++; compteur2++; }}
        if(user.getInt(5) == 1) {
            if (compteur2 == 0) {requete = requete + " and PRIORITE in ('CT',"; compteur++; compteur2++; }
            else if (compteur2 >= 1) { requete = requete + "'CT',";
                compteur++; compteur2++;}}
        if(user.getInt(6) == 1) {
            if (compteur2 == 0) { requete = requete + " and PRIORITE in ('MT',"; compteur++; compteur2++;
            } else if (compteur2 >= 1) {requete = requete + "'MT',";
                compteur++;  compteur2++;}}
        if(user.getInt(7) == 1) {
            if (compteur2 == 0) {requete = requete + " and PRIORITE in ('LT',";  compteur++; compteur2++;}
            else if (compteur2 >= 1) { requete = requete + "'LT',";
                compteur++; compteur2++;} }
        if(compteur2 >= 1){
                requete = requete.substring(0, requete.length() -1);
                requete = requete +")";
        }

        String DLSup = user.getString(11).substring(0, 8)+"31 23:59:59";
        String DLInf = user.getString(11).substring(0, 8)+"01 00:00:00";

        if(user.getInt(10) == 3){
            requete = requete + " and DATELIMITE >= datetime('"+user.getString(11)+"')";
            compteur++; }
        if(user.getInt(10) == 4){
            requete = requete + " and DATELIMITE <= datetime('"+user.getString(11)+"')";
            compteur++; }
        if(user.getInt(10) == 5){
            requete = requete + " and DATELIMITE BETWEEN datetime('"+DLInf+"') AND datetime('"+DLSup+"')";
            compteur++; }

        if(user.getInt(1) == 1){
            if(compteurBy == 0){requete = requete + " order by";}else{requete = requete + ",";}
            requete = requete + " TITRE ASC";
            compteurBy++; }
        if(user.getInt(10) == 0){
            if(compteurBy == 0){requete = requete + " order by";}else{requete = requete + ",";}
            requete = requete + " IDTACHE DESC";
            compteurBy++; }
        if(user.getInt(10) == 1){
            if(compteurBy == 0){requete = requete + " order by";}else{requete = requete + ",";}
            requete = requete + " DATELIMITE == '', DATELIMITE ASC";
            compteurBy++; }
        if(user.getInt(10) == 2){
            if(compteurBy == 0){requete = requete + " order by";}else{requete = requete + ",";}
            requete = requete + " DATELIMITE DESC";
            compteurBy++; }
        if(compteur == 0 && newCompteur == 0){requete = "select * from taches where IDTACHE=0";}

        Cursor result = db.rawQuery(requete, null);
        return result;
    }
    public Cursor recupTacheByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resultat = db.rawQuery("select * from "+TACHE_NOM_TABLE+", "+CATEGORIE_NOM_TABLE+" WHERE IDTACHE = "+id+" AND CATEGORIE = IDCAT",null);
        return resultat;
    }
    public void renouvRecurFini(){
        SQLiteDatabase db = this.getWritableDatabase();
        String requete ="select * from "+TACHE_NOM_TABLE+" where PRIORITE = 'R' AND EFFECTUE = 1 AND DATELIMITE <= datetime('"+GestionDates.DateActuelle()+"')";
        Cursor resulta = db.rawQuery(requete, null);
        ContentValues contentValues = new ContentValues();
        int[] id = new int[resulta.getCount()];
        String[] dateLimite = new String[resulta.getCount()];
        String[] intervalle = new String[resulta.getCount()];
        if (resulta.moveToFirst()) {
            int i = 0;
            do {
                id[i] = resulta.getInt(0);
                dateLimite[i] = resulta.getString(6);
                intervalle[i] = resulta.getString(7);
                contentValues.put("DATECREATION", dateLimite[i]);
                contentValues.put("DATELIMITE", GestionDates.CalculerDateLimite(dateLimite[i], intervalle[i]));
                contentValues.put("EFFECTUE", 0);
                contentValues.put("NOTIF", 0);
                db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE="+id[i], null);
                Log.i("Information", "Une tache récurrente a été mise à jour automatiquement !");
            } while (resulta.moveToNext());
        }
        db.close();
    }
    public void renouvQuotid(){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("Steps", "RenouvQuotid(SQL) a été apellé");
        if(GestionDates.ApresMinuit(Calendar.getInstance().getTime())) {
            Log.i("Steps", "Nous sommes entre 00:00 et 02:00");
            String requete = "select * from " + TACHE_NOM_TABLE + " where PRIORITE = 'Q'";
            Cursor resulta = db.rawQuery(requete, null);
            ContentValues contentValues = new ContentValues();
            int[] id = new int[resulta.getCount()];
            if (resulta.moveToFirst() == true) {
                Log.i("Steps", "Au moins une tache quotidienne a été trouvée");
                int i = 0;
                do {
                    id[i] = resulta.getInt(0);
                    contentValues.put("EFFECTUE", 0);
                    db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE=" + id[i], null);
                    Log.i("Information", "Une tache quotidienne a été mise à jour automatiquement !");
                } while (resulta.moveToNext());
            }
        }
        db.close();
    }
    public Cursor renouvRecurPasFini(){
        SQLiteDatabase db = this.getWritableDatabase();
        String requete ="select * from "+TACHE_NOM_TABLE+" where PRIORITE = 'R' AND EFFECTUE = 0 AND DATELIMITE <= datetime('"+GestionDates.DateActuelle()+"')";
        Cursor resulta = db.rawQuery(requete, null);
        ContentValues contentValues = new ContentValues();
        int[] id = new int[resulta.getCount()];
        String[] dateLimite = new String[resulta.getCount()];
        String[] intervalle = new String[resulta.getCount()];
        if (resulta.moveToFirst()) {
            int i = 0;
            do {
                id[i] = resulta.getInt(0);
                dateLimite[i] = resulta.getString(6);
                intervalle[i] = resulta.getString(7);
                contentValues.put("DATECREATION", dateLimite[i]);
                contentValues.put("DATELIMITE", GestionDates.CalculerDateLimite(dateLimite[i], intervalle[i]));
                contentValues.put("NOTIF", 0);
                db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE="+id[i], null);
                Log.i("Information", "Une tache récurrente a été mise à jour automatiquement, elle n'a pas ete finie !");
            } while (resulta.moveToNext());
        }
        return resulta;
    }
    public void supprimerTache(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TACHE_NOM_TABLE, "IDTACHE" + " = " + id, null);
        db.close();
    }
    public void setEffectue(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EFFECTUE", 1);
        db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE="+id, null);
        // TODO + Jampoints ?
        db.close();
    }
    public Cursor setNonEffectue(){
        SQLiteDatabase db = this.getWritableDatabase();
        String requete ="select * from "+TACHE_NOM_TABLE+" where DATELIMITE != '' AND DATELIMITE <= datetime('"+GestionDates.DateActuelle()+"') AND EFFECTUE = 0 AND PRIORITE in ('CT','MT','LT')";
        Cursor resulta = db.rawQuery(requete, null);
        ContentValues contentValues = new ContentValues();
        int[] id = new int[resulta.getCount()];
        if (resulta.moveToFirst()) {
            int i = 0;
            do {
                id[i]=resulta.getInt(0);
                contentValues.put("EFFECTUE", 2);
                db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE="+id[i], null);
                Log.i("Information", "Une tache normale n'a pas ete finie !");
            } while (resulta.moveToNext());
        }
        return resulta;
        // TODO - Jampoints ?
    }
    public void setPause(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATELIMITE", "");
        db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE="+id, null);
        db.close();
    }
    public void setRenouvelle(int id, String dateLimite, String dateRenouv){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATECREATION", dateRenouv);
        contentValues.put("DATELIMITE", dateLimite);
        contentValues.put("EFFECTUE", 0);
        contentValues.put("NOTIF", 0);
        db.update(TACHE_NOM_TABLE, contentValues, "IDTACHE="+id, null);
        db.close();
    }

}