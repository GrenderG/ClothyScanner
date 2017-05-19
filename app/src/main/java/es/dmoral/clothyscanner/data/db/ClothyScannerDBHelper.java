package es.dmoral.clothyscanner.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.dmoral.clothyscanner.data.model.Machine;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * This file is part of ClothyScanner.
 *
 * ClothyScanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClothyScanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClothyScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ClothyScannerDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "clothy_scanner.db";
    private static final int DB_VERSION = 1;
    private static ClothyScannerDBHelper clothyScannerDBHelper;

    static {
        cupboard().register(Machine.class);
    }

    public ClothyScannerDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
