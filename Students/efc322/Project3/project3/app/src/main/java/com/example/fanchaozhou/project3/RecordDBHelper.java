package com.example.fanchaozhou.project3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Fanchao Zhou on 4/5/2016.
 */
public class RecordDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Samples_Database";
    public static final int DB_VERSION = 1;

    public RecordDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);  //Create a database with the current name and version number
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);  //Enable the foreign key
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //The SQLite Query for creating a new class table
        final String SQL_CREATE_TYPE_TABLE = "CREATE TABLE " + RecordContract.TYPE_TABLE_NAME + " (" +
                RecordContract.TypeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecordContract.TypeEntry.TYPE_NAME + " TEXT NOT NULL, " +
                " UNIQUE (" + RecordContract.TypeEntry.TYPE_NAME + ")" +    //The name of the class should be unique
                ");";

        //The SQLite Query for creating a new record table
        final String SQL_CREATE_RECORD_TABLE = "CREATE TABLE " + RecordContract.RECORD_TABLE_NAME + " (" +
                RecordContract.RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecordContract.RecordEntry.TYPE_NAME + " TEXT NOT NULL, " +
                RecordContract.RecordEntry.TYPE_ID + " INTEGER NOT NULL, " +
                RecordContract.RecordEntry.PHOTO_DIR + " TEXT NOT NULL, " +
                RecordContract.RecordEntry.THUMBNAIL_PHOTO + " BLOB NOT NULL,  "+
                " FOREIGN KEY (" + RecordContract.RecordEntry.TYPE_ID + ") REFERENCES " +
                RecordContract.TYPE_TABLE_NAME + " (" + RecordContract.TypeEntry._ID + ") " +
                ");";

        db.execSQL(SQL_CREATE_TYPE_TABLE);
        db.execSQL(SQL_CREATE_RECORD_TABLE);

        System.out.println("Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //If the database is upgraded, then the current sample set is simply dropped
        db.execSQL("DROP TABLE IF EXISTS " + RecordContract.TYPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecordContract.RECORD_TABLE_NAME);

        onCreate(db);
    }

    public void readAllRecs(final ArrayList<DBRecord> list){
        final SQLiteOpenHelper dbHelper = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.query(
                        RecordContract.RECORD_TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        RecordContract.RecordEntry.TYPE_ID + " ASC"
                );

                c.moveToFirst();
                while(!c.isAfterLast()){
                    DBRecord rec = new DBRecord();
                    rec.recordID = c.getLong(c.getColumnIndex(RecordContract.RecordEntry._ID));
                    rec.typeID = c.getLong(c.getColumnIndex(RecordContract.RecordEntry.TYPE_ID));
                    rec.typeName = c.getString(c.getColumnIndex(RecordContract.RecordEntry.TYPE_NAME));
                    rec.fullsizePhotoUri = c.getString(c.getColumnIndex(RecordContract.RecordEntry.PHOTO_DIR));

                    byte[] imageByteArray = c.getBlob(c.getColumnIndex(RecordContract.RecordEntry.THUMBNAIL_PHOTO));
                    rec.thumbnailPhoto = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

                    list.add(rec);
                    c.moveToNext();
                }

                c.close();
            }
        });

        thread.start();
    }

    public void readAllTypes(final ArrayList<DBType> list){
        final SQLiteOpenHelper dbHelper = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.query(RecordContract.TYPE_TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        RecordContract.TypeEntry._ID + " ASC");

                c.moveToFirst();
                while(!c.isAfterLast()){
                    DBType type = new DBType();
                    type.typeID = c.getLong(c.getColumnIndex(RecordContract.TypeEntry._ID));
                    type.typeName = c.getString(c.getColumnIndex(RecordContract.TypeEntry.TYPE_NAME));
                    list.add(type);
                    c.moveToNext();
                }

                c.close();
            }
        });

        thread.start();
    }

    public void addOneRec(final DBRecord rec){
        final SQLiteOpenHelper dbHelper = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                rec.thumbnailPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                try {
                    stream.close();
                } catch (Exception e){
                    System.out.println(e);
                }

                values.put(RecordContract.RecordEntry.TYPE_NAME, rec.typeName);
                values.put(RecordContract.RecordEntry.TYPE_ID, rec.typeID);
                values.put(RecordContract.RecordEntry.THUMBNAIL_PHOTO, byteArray);
                values.put(RecordContract.RecordEntry.PHOTO_DIR, rec.fullsizePhotoUri);
                rec.recordID = db.insert(RecordContract.RECORD_TABLE_NAME, null, values);
            }
        });

        thread.start();
    }

    public void addOneType(final DBType type){
        final SQLiteOpenHelper dbHelper = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(RecordContract.TypeEntry.TYPE_NAME, type.typeName);
                type.typeID = db.insert(RecordContract.TYPE_TABLE_NAME, null, values);
            }
        });

        thread.start();
    }

    public void deleteOneType(final long typeID){
        final SQLiteOpenHelper dbHelper = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String selectionRec = RecordContract.RecordEntry.TYPE_ID + " = ?";
                String selectionType = RecordContract.TypeEntry._ID + " = ?";
                String[] selectionArgs = {""+typeID};

                int recNum = db.delete(RecordContract.RECORD_TABLE_NAME, selectionRec, selectionArgs);
                int typeNum = db.delete(RecordContract.TYPE_TABLE_NAME, selectionType, selectionArgs);
            }
        });

        thread.start();
    }

    public void deleteOneRec(final long recID){
        final SQLiteOpenHelper dbHelper = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String selectionRec = RecordContract.RecordEntry._ID + " = ?";
                String[] selectionArgs = {""+recID};

                db.delete(RecordContract.RECORD_TABLE_NAME, selectionRec, selectionArgs);
            }
        });

        thread.start();
    }
}
