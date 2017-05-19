package es.dmoral.clothyscanner.data.access;

import android.database.Cursor;

import java.util.ArrayList;

import es.dmoral.clothyscanner.data.db.ClothyScannerDBHelper;
import es.dmoral.clothyscanner.data.model.Machine;
import nl.qbusict.cupboard.QueryResultIterable;

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

@SuppressWarnings("TryFinallyCanBeTryWithResources")
public class DBAccessor {
    public static ArrayList<Machine> getMachines(ClothyScannerDBHelper clothyScannerDBHelper) {
        ArrayList<Machine> machines = new ArrayList<>();

        Cursor machineCursor = cupboard().withDatabase(clothyScannerDBHelper.getReadableDatabase())
                .query(Machine.class).getCursor();

        try {
            QueryResultIterable<Machine> itr = cupboard().withCursor(machineCursor).iterate(Machine.class);
            for (Machine machine : itr) {
                machines.add(machine);
            }
        } finally {
            machineCursor.close();
        }

        return machines;
    }

    public static void addMachine(Machine machine, ClothyScannerDBHelper clothyScannerDBHelper) {
        cupboard().withDatabase(clothyScannerDBHelper.getWritableDatabase()).put(machine);
    }

    public static void deleteMachine(Machine machine, ClothyScannerDBHelper clothyScannerDBHelper) {
        cupboard().withDatabase(clothyScannerDBHelper.getWritableDatabase()).delete(Machine.class, "ip = ?", machine.getIp());
    }
}
