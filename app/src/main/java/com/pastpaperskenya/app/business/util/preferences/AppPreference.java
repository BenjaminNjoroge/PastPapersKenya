package com.pastpaperskenya.app.business.util.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pastpaperskenya.app.R;
import com.pastpaperskenya.app.business.model.Download;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AppPreference {

    // declare context
    private static Context mContext;

    // singleton
    private static AppPreference appPreference = null;

    // common
    private SharedPreferences sharedPreferences, settingsPreferences;
    private SharedPreferences.Editor editor;

    private AppPreference() {
        sharedPreferences = mContext.getSharedPreferences(PrefKey.APP_PREFERENCE, Context.MODE_PRIVATE);
        settingsPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = sharedPreferences.edit();
    }

    public static AppPreference getInstance(Context context) {
        if (appPreference == null) {
            mContext = context;
            appPreference = new AppPreference();
        }
        return appPreference;
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void setBooleanForPush(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setInteger(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void setDownloadList(List<Download> downloadData){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        List<Download> textList = new ArrayList<Download>(downloadData);
        String jsonText = gson.toJson(textList);
        editor.putString("key", jsonText);
        editor.apply();
    }

    public List<Download> getDownloadList(){
        Gson gson = new Gson();
        String jsonText = sharedPreferences.getString("key", null);
        /*List<DownloadData> text = (List<DownloadData>) gson.fromJson(jsonText, DownloadData.class);
        return text;*/
        Type type = new TypeToken<ArrayList<Download>>() {}.getType();
        return gson.fromJson(jsonText, type);
    }

}
