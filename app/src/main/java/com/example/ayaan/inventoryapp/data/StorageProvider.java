package com.example.ayaan.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ayaan.inventoryapp.data.StorageContract.StorageEntry;
/**
 * Created by AYAAN on 5/4/2017.
 */

public class StorageProvider extends ContentProvider{

    private static final int STORAGE = 50;
    private static final int STORAGE_ID = 51;
    StorageDbHelper mDbHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(StorageContract.CONTENT_AUTHORITY,StorageContract.PATH_STORAGE,STORAGE);

        uriMatcher.addURI(StorageContract.CONTENT_AUTHORITY,StorageContract.PATH_STORAGE + "/#",STORAGE_ID);
    }







    @Override
    public boolean onCreate() {
        mDbHelper = new StorageDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,String[] projection, String selection,String[] stringArgs,String sortOrder) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getReadableDatabase();

        Cursor cursor ;

        int match = uriMatcher.match(uri);
        switch (match)
        {
            case STORAGE:
                cursor = sqLiteDatabase.query(StorageEntry.TABLE_NAME,projection,selection,stringArgs,null,null,sortOrder);
                break;
            case STORAGE_ID:
                selection = StorageEntry._ID + "=?";
                stringArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                cursor =sqLiteDatabase.query(StorageEntry.TABLE_NAME,projection,selection,stringArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case STORAGE:
                return StorageEntry.CONTENT_LIST_TYPE;
            case STORAGE_ID:
                return StorageEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uri ??");
        }
    }

    @Override
    public Uri insert(Uri uri,ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case STORAGE:
                SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
                long id = sqLiteDatabase.insert(StorageEntry.TABLE_NAME,null,contentValues);

                if(id == -1){
                    Log.e("Failed to insert values"+uri,"---");
                    return null;
                }
                else {
                    getContext().getContentResolver().notifyChange(uri,null);
                    return ContentUris.withAppendedId(uri,id);
                }
                default:
                    throw new IllegalArgumentException("Cannot insert values");
        }
    }

    @Override
    public int delete(Uri uri,String s,String[] strings) {
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowDelete;
        switch (match){
            case STORAGE:
                rowDelete = sqLiteDatabase.delete(StorageEntry.TABLE_NAME,s,strings);
                break;
            case STORAGE_ID:
                s = StorageEntry._ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowDelete = sqLiteDatabase.delete(StorageEntry.TABLE_NAME,s,strings);
                break;
            default:
                throw new IllegalArgumentException("Delete failed"+uri);
        }
        if (rowDelete!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowDelete;
    }

    @Override
    public int update(Uri uri,ContentValues contentValues,String s,String[] strings) {
        final int match = uriMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = mDbHelper.getWritableDatabase();
        int rowUpdate;
        switch (match){
            case STORAGE:
                rowUpdate = sqLiteDatabase.update(StorageEntry.TABLE_NAME,contentValues,s,strings);
                if ( rowUpdate != 0 ){
                    return rowUpdate;
                }
            case STORAGE_ID:
                s = StorageEntry._ID + "=?";
                strings = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowUpdate = sqLiteDatabase.update(StorageEntry.TABLE_NAME,contentValues,s,strings);
                if ( rowUpdate != 0 ){
                    return rowUpdate;
                }
                default:
                    throw new IllegalArgumentException("Cannot update with uri"+uri);
        }
    }

}
