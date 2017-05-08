package com.example.ayaan.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by AYAAN on 5/4/2017.
 */

public final class StorageContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_STORAGE = "storage";

    public static final class StorageEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"+CONTENT_AUTHORITY+ "/" + PATH_STORAGE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/"+PATH_STORAGE;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_STORAGE);

        public static final String TABLE_NAME = "storage";
        public static final String _ID = "_id";
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_IMAGE = "image";
        public static final String COLUMN_PHONE_NUMBER = "number";
    }
}
