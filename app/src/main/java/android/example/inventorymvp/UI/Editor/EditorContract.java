package android.example.inventorymvp.UI.Editor;

import android.content.DialogInterface;
import android.example.inventorymvp.Model.Inventory;
import android.view.View;

public interface EditorContract {
    interface Presenter {
        void update(Inventory inventory);

    }

    interface View {
        void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener);
        void saveInventory();
        void dispatchTakePictureIntent(android.view.View view);
    }
}
