package android.example.inventorymvp.data;

import android.content.Context;
import android.example.inventorymvp.Model.Inventory;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Inventory.class}, version = 1, exportSchema = false)
public abstract class InventoryDatabase extends RoomDatabase {

    private static InventoryDatabase instance;

    public abstract InventoryDao inventoryDao();

    public static synchronized  InventoryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    InventoryDatabase.class, "inventory_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };


}
