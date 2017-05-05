package com.example.ayaan.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ayaan.inventoryapp.data.StorageContract.StorageEntry;
import static android.R.attr.version;

/**
 * Created by AYAAN on 5/4/2017.
 */

public class StorageDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "storage.db";

    public StorageDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE "+StorageEntry.TABLE_NAME+" ("
                +StorageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +StorageEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL,"
                +StorageEntry.COLUMN_PRODUCT_QUANTITY + " INT NOT NULL,"
                +StorageEntry.COLUMN_PRODUCT_IMAGE + " TEXT, "
                +StorageEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL DEFAULT 0);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
