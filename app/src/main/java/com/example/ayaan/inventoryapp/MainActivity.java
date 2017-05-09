package com.example.ayaan.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.ayaan.inventoryapp.data.StorageContract.StorageEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    StorageAdapter mStorageAdapter;
    private static final int Storage_Loader = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,StorageEditor.class);
                startActivity(intent);
            }
        });
        ListView listView = (ListView)findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_list);
        listView.setEmptyView(emptyView);
        mStorageAdapter = new StorageAdapter(this,null);
        listView.setAdapter(mStorageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,StorageEditor.class);
                Uri currentUri = ContentUris.withAppendedId(StorageEntry.CONTENT_URI,l);
                Log.v("MainActivity : ", currentUri.toString() );
                intent.setData(currentUri);
                startActivity(intent);

            }
        });
        getLoaderManager().initLoader(Storage_Loader,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = { StorageEntry._ID,
                        StorageEntry.COLUMN_PRODUCT_NAME,
                        StorageEntry.COLUMN_PRODUCT_QUANTITY,
                        StorageEntry.COLUMN_PRODUCT_PRICE,
                        StorageEntry.COLUMN_PRODUCT_IMAGE,
                        StorageEntry.COLUMN_PHONE_NUMBER
        };
        return new CursorLoader(this,StorageEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("Data fetched "+StorageEntry.COLUMN_PRODUCT_NAME,"---");
        mStorageAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mStorageAdapter.swapCursor(null);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.dummy_data_menu:
                ContentValues contentValues = new ContentValues();
                contentValues.put(StorageEntry.COLUMN_PRODUCT_NAME,"Laptop");
                contentValues.put(StorageEntry.COLUMN_PRODUCT_PRICE,"25000");
                contentValues.put(StorageEntry.COLUMN_PRODUCT_QUANTITY,"22");
                contentValues.put(StorageEntry.COLUMN_PHONE_NUMBER,"8439882502");
                Uri uri1 = Uri.parse("android.resource://com.example.ayaan.inventoryapp/drawable/macbook");
                contentValues.put(StorageEntry.COLUMN_PRODUCT_IMAGE,uri1.toString());
                Uri uri = getContentResolver().insert(StorageEntry.CONTENT_URI,contentValues);
                return true;
            case R.id.delete_all_menu:
                deleteDialogBox();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete all Products?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteall();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null){
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteall(){
        getContentResolver().delete(StorageEntry.CONTENT_URI,null,null);
    }
}
