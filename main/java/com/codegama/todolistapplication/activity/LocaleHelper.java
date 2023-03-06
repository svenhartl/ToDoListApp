package com.codegama.todolistapplication.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper{
   private Context ct;
   private SharedPreferences sharedPreferences;

   public LocaleHelper(Context ctx){

       ct=ctx;
       sharedPreferences=ct.getSharedPreferences("Lang",Context.MODE_PRIVATE);
   }

   public void updateResource(String code){

       Locale locale=new Locale(code);
       Locale.setDefault(locale);
       Resources resources=ct.getResources();
       Configuration configuration=resources.getConfiguration();
       configuration.locale=locale;
       resources.updateConfiguration(configuration,resources.getDisplayMetrics());
       setLang(code);
   }

   public void setLang(String code){

       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString("lang",code);
       editor.commit();
   }

   public String getLang(){

       return sharedPreferences.getString("lang","en");

   }
}