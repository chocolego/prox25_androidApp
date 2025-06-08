package proxecto25.prox25_mcl.bbdd;

import android.provider.BaseColumns;

public class prefsContract {

    prefsContract(){};

    public static class PrefsTabla implements BaseColumns {
        public static final String TABLE_NAME= "Prefs";
        public static final String COLUMN_NAME_NAME="name";
        public static final String COLUMN_NAME_PASS="pass";
        public static final String COLUMN_NAME_IP="ip";
        public static final String COLUMN_NAME_PORT="port";
        public static final String COLUMN_NAME_LANGUAGE="idioma";
        public static final String COLUMN_NAME_IMAGEPROF="imagenprof";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + PrefsTabla.TABLE_NAME + "(" +
                        PrefsTabla._ID + " INTEGER PRIMARY KEY, " +
                        PrefsTabla.COLUMN_NAME_NAME + " TEXT, " +
                        PrefsTabla.COLUMN_NAME_PASS + " TEXT, " +
                        PrefsTabla.COLUMN_NAME_IP + " TEXT, " +
                        PrefsTabla.COLUMN_NAME_PORT + " TEXT, " +
                        PrefsTabla.COLUMN_NAME_LANGUAGE + " TEXT, " +
                        PrefsTabla.COLUMN_NAME_IMAGEPROF + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + PrefsTabla.TABLE_NAME;
    }
}
