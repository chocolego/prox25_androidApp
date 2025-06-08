package proxecto25.prox25_mcl.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import proxecto25.prox25_mcl.bbdd.prefsDAO;
import proxecto25.prox25_mcl.modelo.Prefs;

public class UtilController {

    static SQLiteDatabase db;
    public static String getBaseConnectionString(Context context, SQLiteDatabase db, String endpoint){
        prefsDAO prefsDAO = new prefsDAO();
        Prefs prefs = prefsDAO.getPreferences(db, context);
        String connectionString = "http://"+prefs.getIp()+":"+prefs.getPort()+"/"+endpoint;
        return connectionString;
    }

    public static void setUserIdPreferences(Context context, String userId){
        //context = getBaseContext
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

    public static String getUserIdFromPreferences(Context context){
        //context = getBaseContext
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "-1");
    }

}
