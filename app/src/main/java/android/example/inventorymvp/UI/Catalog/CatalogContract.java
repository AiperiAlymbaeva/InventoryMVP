package android.example.inventorymvp.UI.Catalog;

import android.example.inventorymvp.Model.Inventory;

public interface CatalogContract {
    interface Presenter {
        void insert(Inventory inventory);
        void update(Inventory inventory);
        void delete(Inventory inventory);
        void deleteAllInventories();

    }

    interface View {

    }
}
