package com.example.ayaan.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayaan.inventoryapp.data.StorageContract;
import com.example.ayaan.inventoryapp.data.StorageContract.StorageEntry;
import com.example.ayaan.inventoryapp.data.StorageProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.R.attr.name;
import static android.os.Build.VERSION_CODES.M;

public class StorageEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private int PICK_IMAGE_REQUEST = 1;
    private Uri mCurrentUri;
    private EditText mName;
    private EditText mQuantity;
    private EditText mPrice;
    private ImageView mImageView;
    private Button add;
    private Button subtract;
    private EditText mNumber;
    private Boolean image_status;
    Uri mImage;
    private Button mOrderMore;
    private  static final int existingStorage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_editor);
        mOrderMore = (Button)findViewById(R.id.order_more);
        final Intent intent = getIntent();
        mCurrentUri = intent.getData();
        if(mCurrentUri == null)
        {
            setTitle("Add Product");
            mOrderMore.setVisibility(View.GONE);
            invalidateOptionsMenu();
            image_status = false;
        }
        else {
            setTitle("Edit Product");
            image_status = true;
            getLoaderManager().initLoader(existingStorage,null,this);
        }
        mName = (EditText)findViewById(R.id.product_name_edit);
        mPrice = (EditText)findViewById(R.id.price_edit);
        mImageView = (ImageView)findViewById(R.id.product_image);
        mQuantity = (EditText) findViewById(R.id.edit_quantity);
        mNumber = (EditText)findViewById(R.id.product_phone_number);
        add = (Button)findViewById(R.id.add);
        subtract = (Button)findViewById(R.id.sub);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mQuantity.getText().toString());
                quantity = quantity+1;
                mQuantity.setText(String.valueOf(quantity));
            }
        });
        subtract.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int quantity = Integer.parseInt(mQuantity.getText().toString());
                if(quantity>0){
                    quantity = quantity-1;
                    mQuantity.setText(String.valueOf(quantity));}
                else{

                    Toast.makeText(StorageEditor.this,"Products cannot be less than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button image = (Button)findViewById(R.id.browse_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);


                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });
        final Uri number = intent.getData();
        mOrderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] projection = {StorageEntry._ID,StorageEntry.COLUMN_PHONE_NUMBER};
                Cursor cursor =getContentResolver().query(number,projection,null,null,null);
                if (cursor.moveToFirst()){
                    int phone_index = cursor.getColumnIndex(StorageEntry.COLUMN_PHONE_NUMBER);
                    Intent intent1 = new Intent(Intent.ACTION_DIAL);
                    String phone = cursor.getString(phone_index);
                    long number = Long.parseLong(phone);
                    intent1.setData(Uri.parse("tel:"+number));
                    startActivity(intent1);
                    Log.e("Phone number"+number,";(");
                }
            }
        });

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
                showDeleteDialog();
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
    private void delete()
    {
        if(mCurrentUri != null)
        {
            int rowDelete = getContentResolver().delete(mCurrentUri,null,null);
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                StorageEntry.COLUMN_PRODUCT_NAME,
                StorageEntry.COLUMN_PRODUCT_QUANTITY,
                StorageEntry.COLUMN_PRODUCT_PRICE,
                StorageEntry.COLUMN_PRODUCT_IMAGE,
                StorageEntry.COLUMN_PHONE_NUMBER
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
            int numberColumnIndex = data.getColumnIndex(StorageEntry.COLUMN_PHONE_NUMBER);
            String name = data.getString(nameColumnindex);
            String quantity = data.getString(quantityColumnIndex);
            String price = data.getString(priceColumnIndex);
            String number = data.getString(numberColumnIndex);
            mName.setText(name);
            mPrice.setText(price);
            mQuantity.setText(quantity);
            mNumber.setText(number);
            //mImageView.setImageBitmap(getBitmap(mCurrentUri));
        }
    }
    public Bitmap getBitmap(Uri uri) {
        if (uri == null || uri.toString().isEmpty())
            return null;
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();
        InputStream inputStream = null;
        try {
            inputStream = this.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            int imageW = options.outWidth;
            int imageH = options.outHeight;

            int scale = Math.min(imageW / targetW, imageH / targetH);

            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            options.inPurgeable = true;
            inputStream = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            return bitmap;

        } catch (FileNotFoundException e) {
            Log.e("Failed to load image", ":(");
            return null;
        } catch (Exception e) {
            Log.e("Image load failed", ":(");
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e("Stream Close", "Failed");
            }
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
        String number = mNumber.getText().toString().trim();
        if (mImage != null)
        {
            image = mImage.toString();
        }
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || !image_status && mCurrentUri !=null || TextUtils.isEmpty(number))
        {
            Toast.makeText(this,"Complete data not filled. Product not saved",Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(StorageEntry.COLUMN_PRODUCT_NAME,name);
        contentValues.put(StorageEntry.COLUMN_PRODUCT_QUANTITY,quantity);
        contentValues.put(StorageEntry.COLUMN_PRODUCT_PRICE,price);
        contentValues.put(StorageEntry.COLUMN_PRODUCT_IMAGE,image);
        contentValues.put(StorageEntry.COLUMN_PHONE_NUMBER,number);
        if(mCurrentUri == null) {
            Uri uri = getContentResolver().insert(StorageEntry.CONTENT_URI, contentValues);
            if(uri!= null)
            {
                Toast.makeText(this,"Product Saved",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            int rowAffected = getContentResolver().update(mCurrentUri,contentValues,null,null);
            if (rowAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Product Update Failed",
                        Toast.LENGTH_SHORT).show();
            }
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
    private void showDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
