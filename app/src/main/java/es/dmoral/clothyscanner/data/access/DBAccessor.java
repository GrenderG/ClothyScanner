package es.dmoral.clothyscanner.data.access;

import android.database.Cursor;

import java.util.ArrayList;

import es.dmoral.clothyscanner.data.db.ClothyScannerDBHelper;
import es.dmoral.clothyscanner.data.model.Machine;
import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by grender on 18/05/17.
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
