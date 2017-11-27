package com.luis_santiago.sqlitedatacourse;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.luis_santiago.sqlitedatacourse.database.ProductContractClass;
import com.luis_santiago.sqlitedatacourse.database.ProductContractClass.ProductEntry;

public class NewProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> , View.OnClickListener {

    private EditText nameEditText,priceEditText,supplierNameEditext,
            supplierEmailEditext, supplierNumberEditext;
    private Uri mCurrentProductUri;
    private TextView mQuantityTextView;
    private boolean mProductHasChanged = false;
    private Button orderButton,plusButton,substractButton;
    private int mQuantity = 0;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        if (mCurrentProductUri == null) {
            Log.e("Product edit", "Im gonna create");
            setTitle("Add a new product");
        } else {
            Log.e("Product edit", "Im gonna update");
            setTitle("Edit product");
            getLoaderManager().initLoader(MainActivity.LOADER_PRODUCT, null, this);
        }

        nameEditText.setOnTouchListener(mTouchListener);
        priceEditText.setOnTouchListener(mTouchListener);
        supplierNameEditext.setOnTouchListener(mTouchListener);
        supplierEmailEditext.setOnTouchListener(mTouchListener);
        supplierNumberEditext.setOnTouchListener(mTouchListener);
        orderButton.setOnClickListener(this);
        plusButton.setOnClickListener(this);
        substractButton.setOnClickListener(this);
    }


    private void updateProduct(){
        String nameProduct = getDatafromEditText(nameEditText);
        int price = Integer.valueOf(getDatafromEditText(priceEditText));
        String supplierName = getDatafromEditText(supplierNameEditext);
        String supplierEmail = getDatafromEditText(supplierEmailEditext);
        int numberOfSupplier = Integer.valueOf(getDatafromEditText(supplierNumberEditext));

        ContentValues values = new ContentValues();

        values.put(ProductEntry.COLUMN_NAME_ , nameProduct);
        values.put(ProductEntry.COLUMN_PRICE , price);
        values.put(ProductEntry.COLUMN_QUANTITY , mQuantity);
        values.put(ProductEntry.COLUMN_SUPPLIER_NAME , supplierName);
        values.put(ProductEntry.COLUMN_SUPPLIER_EMAIL , supplierEmail);
        values.put(ProductEntry.COLUMN_SUPPLIER_NUMBER , numberOfSupplier);

        int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

        if (rowsAffected == 0) {
            Toast.makeText(this,"Update failed",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Update Succesful",
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void insertProduct() {
        String nameProduct = getDatafromEditText(nameEditText);
        String supplierName = getDatafromEditText(supplierNameEditext);
        String supplierEmail = getDatafromEditText(supplierEmailEditext);


        if(mCurrentProductUri == null && ( TextUtils.isEmpty(nameProduct) || TextUtils.isEmpty(supplierEmail) || TextUtils.isEmpty(supplierEmail))){
            Toast.makeText(NewProductActivity.this, "There is no data" , Toast.LENGTH_SHORT).show();
        }else{
            int price = 0;
            int numberOfSupplier = 0;

            if(!TextUtils.isEmpty(getDatafromEditText(priceEditText))){
                price = Integer.valueOf(getDatafromEditText(priceEditText));
            }

            if(!TextUtils.isEmpty(getDatafromEditText(supplierNumberEditext))){
                numberOfSupplier = Integer.valueOf(getDatafromEditText(supplierNumberEditext));
            }


            ContentValues values = new ContentValues();

            values.put(ProductEntry.COLUMN_NAME_ , nameProduct);
            values.put(ProductEntry.COLUMN_PRICE , price);
            values.put(ProductEntry.COLUMN_QUANTITY , mQuantity);
            values.put(ProductEntry.COLUMN_SUPPLIER_NAME , supplierName);
            values.put(ProductEntry.COLUMN_SUPPLIER_EMAIL , supplierEmail);
            values.put(ProductEntry.COLUMN_SUPPLIER_NUMBER , numberOfSupplier);

            Uri newUri = getContentResolver().insert(ProductContractClass.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    }

    private void init(){
        nameEditText = findViewById(R.id.name_product_editText);
        priceEditText = findViewById(R.id.price_product_editText);
        supplierNameEditext = findViewById(R.id.supplier_name_product_editText);
        supplierEmailEditext = findViewById(R.id.supplier_email_product_editText);
        supplierNumberEditext = findViewById(R.id.supplier_number_product_editText);
        orderButton = findViewById(R.id.order_button);
        mQuantityTextView = findViewById(R.id.display_quantity);
        plusButton = findViewById(R.id.add_button);
        substractButton = findViewById(R.id.substract_button);
    }

    private String getDatafromEditText (EditText editText){
        String StringToReturn = editText.getText().toString().trim();
        if(TextUtils.isEmpty(StringToReturn)){
            editText.setError("We need some data");
            return "";
        }
        else{
            return StringToReturn;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String [] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_NAME_,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_EMAIL,
                ProductEntry.COLUMN_SUPPLIER_NUMBER,
        };
        return new CursorLoader(this, mCurrentProductUri, projection , null , null , null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            mCursor = cursor;
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_NAME_);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierEmailColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_EMAIL);
            int supplierNumberColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            mQuantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierEmail = cursor.getString(supplierEmailColumnIndex);
            int supplierNumber = cursor.getInt(supplierNumberColumnIndex);

            //Update the views

            nameEditText.setText(name);
            priceEditText.setText(String.valueOf(price));
            mQuantityTextView.setText(String.valueOf(mQuantity));
            supplierNameEditext.setText(supplierName);
            supplierEmailEditext.setText(supplierEmail);
            supplierNumberEditext.setText(String.valueOf(supplierNumber));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mCurrentProductUri == null){
            setTitle("Create a new product");
            invalidateOptionsMenu();
        }

        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if(mCurrentProductUri ==null){
                    //New Product, call the new method
                    insertProduct();
                }else{
                    // an existing product, update it
                    updateProduct();
                }
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(NewProductActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(NewProductActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_product_successful_delete),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        int currentItem = view.getId();
        switch (currentItem){
            case R.id.order_button:{
                makeOrder();
                break;
            }
            case R.id.add_button:{
                mQuantity++;
                mQuantityTextView.setText(String.valueOf(mQuantity));
                break;
            }

            case R.id.substract_button:{
                if(mQuantity == 0){
                    Toast.makeText(NewProductActivity.this, "Can't be a negative value" , Toast.LENGTH_SHORT).show();
                }
                else{
                    mQuantity--;
                    mQuantityTextView.setText(String.valueOf(mQuantity));
                }
            }
        }
    }

    private void makeOrder() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        if(mCursor!=null){
            int nameColumnIndex = mCursor.getColumnIndex(ProductEntry.COLUMN_NAME_);
            int quantityColumnIndex = mCursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = mCursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierEmailColumnIndex = mCursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_EMAIL);

            String name = mCursor.getString(nameColumnIndex);
            mQuantity = mCursor.getInt(quantityColumnIndex);
            String supplierName = mCursor.getString(supplierNameColumnIndex);
            String supplierEmail = mCursor.getString(supplierEmailColumnIndex);

            String mailto = "mailto:luissantiagodev@gmail.com" +
                    "?cc=" + supplierEmail +
                    "&subject=" + Uri.encode("New order") +
                    "&body=" + Uri.encode("I would like to order " + name + " and I would like" + mQuantity + "of it from our supplier" + supplierName);

            emailIntent.setData(Uri.parse(mailto));

            try {
                startActivity(emailIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(NewProductActivity.this , "Couldn't find and email app" , Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(NewProductActivity.this , "There was an error loading the data" , Toast.LENGTH_SHORT).show();
        }
    }
}
