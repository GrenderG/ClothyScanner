package es.dmoral.clothyscanner.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.dmoral.clothyscanner.data.model.Machine;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by grender on 18/05/17.
 */

public class ClothyScannerDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "clothy_scanner.db";
    private static final int DB_VERSION = 1;
    private static ClothyScannerDBHelper clothyScannerDBHelper;

    public ClothyScannerDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    static {
        cupboard().register(Machine.class);
    }

    public static ClothyScannerDBHelper getClothyScannerDBHelper(Context context) {
        if (clothyScannerDBHelper == null)
            clothyScannerDBHelper = new ClothyScannerDBHelper(context);
        return clothyScannerDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        cupboard().withDatabase(sqLiteDatabase).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        cupboard().withDatabase(sqLiteDatabase).upgradeTables();
    }
}
