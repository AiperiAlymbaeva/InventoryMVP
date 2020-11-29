package android.example.inventorymvp.UI.Editor;

import android.app.Application;
import android.example.inventorymvp.Model.Inventory;
import android.example.inventorymvp.Repository.InventoryRepository;
import android.example.inventorymvp.data.InventoryDao;
import android.example.inventorymvp.data.InventoryDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EditorPresenter extends AndroidViewModel implements EditorContract.Presenter {
    private InventoryRepository repository;
    LiveData<Inventory> inventoryLiveData;

    public EditorPresenter(@NonNull Application application) {
        super(application);
        repository = new InventoryRepository(application);
    }

    @Override
    public void update(Inventory inventory) {
        repository.update(inventory);
    }






}
