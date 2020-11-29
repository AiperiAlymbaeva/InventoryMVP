package android.example.inventorymvp.UI.Editor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.example.inventorymvp.Model.Inventory;
import android.example.inventorymvp.R;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditorActivity extends AppCompatActivity implements EditorContract.View {
    public static final String EXTRA_ID = " package android.example.inventorymvp.EXTRA_ID";
    public static final String EXTRA_NAME = "package android.example.inventorymvp.EXTRA_NAME";
    public static final String EXTRA_MODEL = "package android.example.inventorymvp.EXTRA_MODEL";
    public static final String EXTRA_PRICE = "package android.example.inventorymvp.EXTRA_PRICE";
    public static final String EXTRA_QUANTITY = "package android.example.inventorymvp.EXTRA_QUANTITY";
    public static final String EXTRA_SUPPLIER = "package android.example.inventorymvp.EXTRA_SUPPLIER";
    public static final String EXTRA_IMAGE = "package android.example.inventorymvp.EXTRA_IMAGE";

    public static int CAMERA_INTENT = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean mInventoryHasChanged = false;
    private EditorPresenter editorPresenter;

    private File fileDirImg;
    private File img;
    private String currentPhotoPath;
    private Uri photoURI;


    private EditText editText_title;
    private EditText editText_model;
    private EditText editText_quantity;
    private EditText editText_supplier;
    private Bitmap bitmapImage;
    private EditText editText_price;
    private Button btnImg;
    private ImageView image_view_takePhoto;
    private String pictureImagePath = "";

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mInventoryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editorPresenter = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(EditorPresenter.class);

        editText_title = findViewById(R.id.edit_inventory_name);
        editText_model = findViewById(R.id.edit_model);
        editText_price = findViewById(R.id.edit_inventory_price);
        editText_supplier = findViewById(R.id.edit_inventory_supplier);
        btnImg = findViewById(R.id.button_takePhoto);
        bitmapImage = null;
        editText_quantity = findViewById(R.id.edit_quantity);
        image_view_takePhoto = findViewById(R.id.image_view_takePhoto);

        editText_title.setOnTouchListener(mTouchListener);
        editText_model.setOnTouchListener(mTouchListener);
        editText_price.setOnTouchListener(mTouchListener);
        editText_supplier.setOnTouchListener(mTouchListener);
        editText_quantity.setOnTouchListener(mTouchListener);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {

            setTitle("Edit Note");
            editText_title.setText(intent.getStringExtra(EXTRA_NAME));
            editText_model.setText(intent.getStringExtra(EXTRA_MODEL));
            editText_supplier.setText(intent.getStringExtra(EXTRA_SUPPLIER));
            editText_price.setText(String.valueOf(intent.getIntExtra(EXTRA_PRICE, 1)));
            editText_quantity.setText(String.valueOf(intent.getIntExtra(EXTRA_QUANTITY, 1)));
        } else {
            setTitle("Add Note");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveInventory();
                return true;
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mInventoryHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(this).load(img.getAbsolutePath()).into(image_view_takePhoto);
        }
    }


    @Override
    public void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mInventoryHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public void saveInventory() {

        String title = editText_title.getText().toString();
        String model = editText_model.getText().toString();
        String supplier = editText_supplier.getText().toString();
        int price = Integer.parseInt(editText_price.getText().toString());
        int quantity = Integer.parseInt(editText_quantity.getText().toString());
        String image = photoURI.toString();

        if (title.trim().isEmpty() || model.trim().isEmpty() || supplier.trim().isEmpty() ||
                price == 0 || quantity == 0 ) {
            Toast.makeText(this, "Please input fields", Toast.LENGTH_SHORT)
                    .show();
            return;
        }


        Intent intent = new Intent();
        intent.putExtra(EXTRA_NAME, title);
        intent.putExtra(EXTRA_MODEL, model);
        intent.putExtra(EXTRA_SUPPLIER, supplier);
        intent.putExtra(EXTRA_QUANTITY, quantity);
        intent.putExtra(EXTRA_PRICE, price);

        if (image != null ) {
        intent.putExtra(EXTRA_IMAGE, image);

        }



        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        Inventory inventory = new Inventory(title, model, price, quantity, supplier, image);
        inventory.setId(id);
        if (id != -1) {
            intent.putExtra(EXTRA_ID, id);
            editorPresenter.update(inventory);
        }



        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void dispatchTakePictureIntent(View view) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = getFileDirImg();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "android.example.fileProviders",
                        photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            }
            startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private File getFileDirImg() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        fileDirImg = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        img = File.createTempFile(
                imageFileName,
                ".jpg",
                fileDirImg
        );

        currentPhotoPath = img.getAbsolutePath();
        return img;
    }
}
