package proxecto25.prox25_mcl.bbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class prefsHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PrefsUsuario.db";

    public prefsHelper(Context contexto){
        super(contexto, DATABASE_NAME,null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(prefsContract.PrefsTabla.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(prefsContract.PrefsTabla.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldversion, int newversion){
        onUpgrade(db,oldversion,newversion);
    }

}
