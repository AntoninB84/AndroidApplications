package fr.jampa.jampav2;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GestionDates {
    static SimpleDateFormat FormatDateBase = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat FormatDateSimple = new SimpleDateFormat("dd/MM/yyyy");
    static SimpleDateFormat FormatDateLong = new SimpleDateFormat("EEEE dd MMM yyyy");
    static SimpleDateFormat FormatHeure = new SimpleDateFormat("HH:mm");

    static String DateActuelle(){
        Calendar dateTime = Calendar.getInstance();
        DecimalFormat mFormat = new DecimalFormat("00");
        int Annee, Mois, Jour, Heure, Minutes, Secondes;
        Annee = dateTime.get(Calendar.YEAR);
        Mois = dateTime.get(Calendar.MONTH)+1;
        Jour = dateTime.get(Calendar.DAY_OF_MONTH);
        Heure = dateTime.get(Calendar.HOUR_OF_DAY);
        Minutes = dateTime.get(Calendar.MINUTE);
        Secondes = dateTime.get(Calendar.SECOND);
        String RetourDate = (Annee+"-"+mFormat.format(Mois)+"-"+mFormat.format(Jour)+" "+Heure+":"+Minutes+":"+Secondes);
        return RetourDate;
    }
    static String ChangerFormatDateSimple(String dateRecue){
        try{
            Date date = FormatDateBase.parse(dateRecue);
            dateRecue = FormatDateSimple.format(date);
        }
        catch(ParseException e){}
        return dateRecue;
    }
    static String ChangerFormatDatePhrase(String dateRecue){
        try{
            Date date = FormatDateBase.parse(dateRecue);
            dateRecue = FormatDateLong.format(date);
        }
        catch(ParseException e){}
        return dateRecue;
    }
    static Date TransformerEnDateSimple(String dateRecue){
        //TODO Pour récupérer une variable Date au format "dd/MM/yyyy"
        ChangerFormatDateSimple(dateRecue);
        Date dateRetour=null;
        try{
            dateRetour = FormatDateSimple.parse(dateRecue);
        }
        catch(ParseException e){}
        return dateRetour;
    }
    static Date TransformerEnDate(String dateRecue){
        //TODO Pour récupérer une variable Date au format complet
        Date dateRetour=null;
        try{
            dateRetour = FormatDateBase.parse(dateRecue);
        }
        catch(ParseException e){}
        return dateRetour;
    }
    static long TransformerDateEnLong(Date dateRecue){
        long dateTransformee = dateRecue.getTime();
        return dateTransformee;
    }
    static String TransformerLongEnString(Long dateRecue){
        Date date = new Date(dateRecue);
        String dateRetour = FormatDateBase.format(date);
        return dateRetour;
    }
    static String RecupHeure(String dateRecue){
        try{
            Date date = FormatDateBase.parse(dateRecue);
            dateRecue = FormatHeure.format(date);
        }
        catch(ParseException e){}
        return dateRecue;
    }
    static boolean SuperieurAJour(String dateRecue){ // Je vérifie que la dateLimite ne soit pas passée
        Date date = TransformerEnDate(dateRecue);
        long delaiSurete = 3600000; // 1h
        long dateActuelle = System.currentTimeMillis() + delaiSurete;
        if(TransformerDateEnLong(date)-dateActuelle <= 0){
            return false;
        }else{
            return true;
        }
    }
    static int[] ConvertFromMillis(String intervalle) {
       // Pour traduire l'intervalleRec en jours, semaines, mois, années ....pour afficher la valeur de la récurrence sur tv_priorité si = R.
        long Intervalle = Long.parseLong(intervalle);
        long annee = 31536000000L;
        long mois = 2592000000L;
        long semaine = 604800000;
        long jour = 86400000;
        long heure = 3600000;
        long restant, calculAnnee, calculMois, calculSemaine, calculJour, calculHeure;
        int finalAnnee, finalMois, finalSemaine, finalJour, finalHeure;
        int[] JSMA = new int[]{0, 0, 0, 0, 0};
        if((Intervalle/annee) >= 1){
            calculAnnee = Intervalle/annee;
            finalAnnee = (int)calculAnnee;
            restant = Intervalle%annee;
            calculMois = restant/mois;
            finalMois =(int)calculMois;
            restant = restant%mois;
            calculSemaine = restant/semaine;
            finalSemaine =(int)calculSemaine;
            restant = restant%semaine;
            calculJour = restant/jour;
            finalJour =(int)calculJour;
            restant = restant%jour;
            calculHeure = restant/heure;
            finalHeure=(int)calculHeure;
            JSMA[0] = finalHeure;
            JSMA[1] = finalJour;
            JSMA[2] = finalSemaine;
            JSMA[3] = finalMois;
            JSMA[4] = finalAnnee;
            return JSMA;
        }else if((Intervalle/mois) >= 1){
            calculMois = Intervalle/mois;
            finalMois =(int)calculMois;
            restant = Intervalle%mois;
            calculSemaine = restant/semaine;
            finalSemaine =(int)calculSemaine;
            restant = restant%semaine;
            calculJour = restant/jour;
            finalJour =(int)calculJour;
            restant = restant%jour;
            calculHeure = restant/heure;
            finalHeure=(int)calculHeure;
            JSMA[0] = finalHeure;
            JSMA[1] = finalJour;
            JSMA[2] = finalSemaine;
            JSMA[3] = finalMois;
            JSMA[4] = 0;
            return JSMA;
        }else if((Intervalle/semaine) >= 1){
            calculSemaine = Intervalle/semaine;
            finalSemaine =(int)calculSemaine;
            restant = Intervalle%semaine;
            calculJour = restant/jour;
            finalJour =(int)calculJour;
            restant = restant%jour;
            calculHeure = restant/heure;
            finalHeure=(int)calculHeure;
            JSMA[0] = finalHeure;
            JSMA[1] = finalJour;
            JSMA[2] = finalSemaine;
            JSMA[3] = 0;
            JSMA[4] = 0;
            return JSMA;
        }else if((Intervalle/jour) >= 1){
            calculJour = Intervalle/jour;
            finalJour =(int)calculJour;
            restant = Intervalle%jour;
            calculHeure = restant/heure;
            finalHeure=(int)calculHeure;
            JSMA[0] = finalHeure;
            JSMA[1] = finalJour;
            JSMA[2] = 0;
            JSMA[3] = 0;
            JSMA[4] = 0;
            return JSMA;
        }else if((Intervalle/heure) >= 1){
            calculHeure = Intervalle/heure;
            finalHeure=(int)calculHeure;
            JSMA[0] = finalHeure;
            JSMA[1] = 0;
            JSMA[2] = 0;
            JSMA[3] = 0;
            JSMA[4] = 0;
            return JSMA;
        }else{
            JSMA[0] = 0;
            JSMA[1] = 0;
            JSMA[2] = 0;
            JSMA[3] = 0;
            JSMA[4] = 0;
            return JSMA;
        }
    }
    static boolean ComparaisonDates(String dateRecue, String rappel){
        Date date = TransformerEnDate(dateRecue);
        long rappelAvant = Long.parseLong(rappel);
        long delaiSurete = 300000; //  10 minutes
        long dateActuelle = System.currentTimeMillis() + delaiSurete;
        if(((dateActuelle) >= (TransformerDateEnLong(date)-rappelAvant-1000*60*10)) && ((dateActuelle) <= (TransformerDateEnLong(date)-rappelAvant+1000*60*30))){
            return true;
        }else{
            return false;
        }
    }
    static long DateRappel(String dateLimite, String rappel){
        //Fonctionne ! Donne une date au format long pour les notifications
        //Helas, n'est plus utilisé tant que je n'arriverai pas à planifier une notification sur une date précise
        Date limite = TransformerEnDate(dateLimite);
        long limiteMillis = limite.getTime();
        long dateNotif = limiteMillis - Long.parseLong(rappel);
        return dateNotif;
    }
    static String CalculerDateLimite(String dateCreation, String intervalle){
        Date depart = TransformerEnDate(dateCreation);
        long departMillis = depart.getTime();
        long dateLimite = departMillis + Long.parseLong(intervalle);
        return TransformerLongEnString(dateLimite);
    }
    static String CalculerDateRappel(String dateCreation, String intervalle){
        Date depart = TransformerEnDate(dateCreation);
        long departMillis = depart.getTime();
        long dateLimite = departMillis - Long.parseLong(intervalle);
        return TransformerLongEnString(dateLimite);
    }
    static boolean ApresMinuit(Date dateActuelle){
        Calendar Minuit = Calendar.getInstance();
        Minuit.set(Calendar.HOUR_OF_DAY, 0);
        Minuit.set(Calendar.MINUTE, 01);
        Date minuit = Minuit.getTime();
        Calendar DeuxHeures = Calendar.getInstance();
        DeuxHeures.set(Calendar.HOUR_OF_DAY, 0);
        DeuxHeures.set(Calendar.MINUTE, 30);
        Date deuxHeures = DeuxHeures.getTime();
        if(dateActuelle.after(minuit) && dateActuelle.before(deuxHeures)){
            Log.i("Steps", "Depuis la fonction ApresMinuit : Nous sommes entre 00:00 et 02:00");
            return true;
        }else{
            Log.i("Steps", "Nous ne sommes pas entre 00:00 et 02:00");
            return false;
        }
    }

}
