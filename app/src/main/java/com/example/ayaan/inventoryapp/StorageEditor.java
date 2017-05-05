package com.example.ayaan.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ayaan.inventoryapp.data.StorageContract;
import com.example.ayaan.inventoryapp.data.StorageContract.StorageEntry;
import com.example.ayaan.inventoryapp.data.StorageProvider;

public class StorageEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private int PICK_IMAGE_REQUEST = 1;
    private Uri mCurrentUri;
    private EditText mName;
    private EditText mQuantity;
    private EditText mPrice;
    private ImageView mImageView;
    Uri mImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_editor);
        mName = (EditText)findViewById(R.id.price_edit);
        mPrice = (EditText)findViewById(R.id.price_edit);
        mImageView = (ImageView)findViewById(R.id.product_image);
        mQuantity = (EditText)findViewById(R.id.quantity_edit);
        Button image = (Button)findViewById(R.id.browse_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
            }
        });
        Intent intent = getIntent();
        mCurrentUri = intent.getData();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getMenuInflater().inflate(R.menu.editor_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()){
            case R.id.delete_single:
                break;

            case R.id.save:
                saveProduct();
                finish();
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        return;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                StorageEntry.COLUMN_PRODUCT_NAME,
                StorageEntry.COLUMN_PRODUCT_QUANTITY,
                StorageEntry.COLUMN_PRODUCT_PRICE,
                StorageEntry.COLUMN_PRODUCT_IMAGE
        };
        return new CursorLoader(this,mCurrentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount()<1)
        {
            return;
        }
        if (data.moveToFirst()){
           int nameColumnindex = data.getColumnIndex(StorageEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = data.getColumnIndex(StorageEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = data.getColumnIndex(StorageEntry.COLUMN_PRODUCT_PRICE);
            int imageColumnIndex = data.getColumnIndex(StorageEntry.COLUMN_PRODUCT_IMAGE);

            String name = data.getString(nameColumnindex);
            String quantity = data.getString(quantityColumnIndex);
            String price = data.getString(priceColumnIndex);
            String image = data.getString(imageColumnIndex);

            mName.setText(name);
            mPrice.setText(price);
            mQuantity.setText(quantity);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName.setText("");
        mQuantity.setText("");
        mPrice.setText("");
    }
    private void saveProduct(){
        final int Storage_Loader = 0;
        String name = mName.getText().toString().trim();
        String price = mPrice.getText().toString().trim();
        String quantity = mQuantity.getText().toString().trim();
        String image = "";
        if (mImage != null)
        {
            image = mImage.toString();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(StorageEntry.COLUMN_PRODUCT_NAME,name);
        contentValues.put(StorageEntry.COLUMN_PRODUCT_QUANTITY,quantity);
        contentValues.put(StorageEntry.COLUMN_PRODUCT_PRICE,price);
        contentValues.put(StorageEntry.COLUMN_PRODUCT_IMAGE,image);
        Uri uri = getContentResolver().insert(StorageEntry.CONTENT_URI,contentValues);
        if(uri!= null)
        {
            Toast.makeText(this,"Pet Saved",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int reqCode,int resCode,Intent intent)
    {
        if (reqCode == PICK_IMAGE_REQUEST && resCode == RESULT_OK)
        {
            mImage = intent.getData();
        }
        super.onActivityResult(reqCode,resCode,intent);
    }
}
