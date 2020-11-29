package android.example.inventorymvp.UI.Catalog;

import android.app.Application;
import android.example.inventorymvp.Model.Inventory;
import android.example.inventorymvp.Repository.InventoryRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CatalogPresenter extends AndroidViewModel implements CatalogContract.Presenter {

    private InventoryRepository repository;
    private LiveData<List<Inventory>> allInventories;

    public CatalogPresenter(@NonNull Application application) {
        super(application);
        repository = new InventoryRepository(application);
        allInventories = repository.getAllInventories();
    }

    public LiveData<List<Inventory>> getAllInventories() {
        return allInventories;
    }


    @Override
    public void insert(Inventory inventory) {
        repository.insert(inventory);
    }

    @Override
    public void update(Inventory inventory) {
        repository.update(inventory);
    }

    @Override
    public void delete(Inventory inventory) {
        repository.delete(inventory);
    }

    @Override
    public void deleteAllInventories() {
        repository.deleteAllInventories();
    }
}
