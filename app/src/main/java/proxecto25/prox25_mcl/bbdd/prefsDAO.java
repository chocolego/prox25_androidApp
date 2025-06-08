package proxecto25.prox25_mcl.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.modelo.Prefs;

public class prefsDAO {

    //Ler preferencias
    public Prefs getPreferences(SQLiteDatabase db, Context context){
        Cursor cursor = db.rawQuery("SELECT * FROM " + prefsContract.PrefsTabla.TABLE_NAME, null);
        Prefs pref;
        if (cursor != null) {
            cursor.moveToFirst();
            pref = new Prefs();
            pref.setId(cursor.getInt(0));
            pref.setName(cursor.getString(1));
            pref.setPass(cursor.getString(2));
            pref.setIp(cursor.getString(3));
            pref.setPort(cursor.getString(4));
            pref.setLanguage(cursor.getString(5));
            pref.setImagenprof(cursor.getString(6));

            cursor.close();
            return pref;
        }
        return null;
    }

    //gardar preferencias

    public void setPreferences(SQLiteDatabase db, Context context, Prefs prefs){

        ContentValues values = new ContentValues();
        values.put(prefsContract.PrefsTabla.COLUMN_NAME_NAME,prefs.getName());
        values.put(prefsContract.PrefsTabla.COLUMN_NAME_PASS,prefs.getPass());
        values.put(prefsContract.PrefsTabla.COLUMN_NAME_IP,prefs.getIp());
        values.put(prefsContract.PrefsTabla.COLUMN_NAME_PORT,prefs.getPort());
        values.put(prefsContract.PrefsTabla.COLUMN_NAME_LANGUAGE,prefs.getLanguage());
        values.put(prefsContract.PrefsTabla.COLUMN_NAME_IMAGEPROF,prefs.getImagenprof());

        String selection = prefsContract.PrefsTabla._ID + " = ?";
        String[] selectionArgs = { String.valueOf(prefs.getId()) };

        int count = db.update(
                prefsContract.PrefsTabla.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        if(count == 0){
            Toast.makeText(context, "Error guardando preferencias.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Prefencias guardadas.", Toast.LENGTH_SHORT).show();
        }

    }

    //prefs por defecto
    public void defaultPreferences(SQLiteDatabase db, Context context){

        String imagenperfil = "";

        try{
            db.delete(prefsContract.PrefsTabla.TABLE_NAME,null,null);

            imagenperfil = encodeImageToBase64(context, R.raw.perfil_usuario);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //crear valores
        ContentValues values2  = new ContentValues();

        values2.put(prefsContract.PrefsTabla.COLUMN_NAME_NAME,"");
        values2.put(prefsContract.PrefsTabla.COLUMN_NAME_PASS,"");
        values2.put(prefsContract.PrefsTabla.COLUMN_NAME_IP, "10.0.2.2");   //"127.0.0.1");
        values2.put(prefsContract.PrefsTabla.COLUMN_NAME_PORT, "8080");
        values2.put(prefsContract.PrefsTabla.COLUMN_NAME_LANGUAGE,"ES");
        values2.put(prefsContract.PrefsTabla.COLUMN_NAME_IMAGEPROF, imagenperfil);

        long newRowId = db.insert(prefsContract.PrefsTabla.TABLE_NAME,
                null, values2);
    }

    public String encodeImageToBase64(Context context, int imageId) {
        try {
            InputStream inputStream = context.getResources().openRawResource(imageId);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void defaultPreferencesparaVacio(SQLiteDatabase db, Context context) {
        boolean tablaVacia = true;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + prefsContract.PrefsTabla.TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count > 0) {
                tablaVacia = false;
            }
            cursor.close();
        }
        if (tablaVacia) {
            defaultPreferences(db, context);
        }
    }
}
