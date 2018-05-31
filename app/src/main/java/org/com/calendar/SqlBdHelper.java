package org.com.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static org.com.calendar.DBConstants.*;

public class SqlBdHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public SqlBdHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_EVENTS + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                TITLE + " TEXT ," +
                DATE + " TEXT ," +
                TIME + " TEXT ," +
                DETAILS + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(sqLiteDatabase);
    }


    public boolean checkIfEventExist(String title, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE_EVENTS +" WHERE "+ TITLE +"=?" +" AND " + DATE +"=?", new String[] {title,date});
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

    //create new event
    public void addEvent(Event event){
        ContentValues values = new ContentValues();
        values.put(TITLE, event.getTitle());
        values.put(DATE, event.getDate());
        values.put(TIME, event.getTime());
        values.put(DETAILS, event.getDetails());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_EVENTS,null,values);
        sqLiteDatabase.close();
    }

    //delete all events from table
    public void deleteAllEvents(String date){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + DATE + "=\"" + date + "\";");
    }

    //delete selected events
    public void deleteSelectedEventOnDate(String id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + ID + "=\"" + id + "\";");
    }


    //move date
    public void moveDate(String id , String date){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE " + TABLE_EVENTS +" SET "+ DATE + " =\"" + date +"\"" + " WHERE " + ID + "=\"" + id + "\";");
    }



    //select events on the date
    public Event[] selectEventOnDate(String date){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_EVENTS + " WHERE " + DATE + " LIKE '%" + date + "%'";

        Cursor c = sqLiteDatabase.rawQuery(query,null);
        Event[] events = new Event[c.getCount()];
        //Move to the first row
        c.moveToFirst();
        int arrIndex = 0;

        for(int i=0; i<c.getCount();i++) {
            if (c.getString(c.getColumnIndex(TITLE)) != null) {
                Event temp = new Event();
                temp.setId(c.getString(c.getColumnIndex("_id")));
                temp.setTitle(c.getString(c.getColumnIndex(TITLE)));
                temp.setDate(c.getString(c.getColumnIndex(DATE)));
                temp.setTime(c.getString(c.getColumnIndex(TIME)));
                temp.setDetails(c.getString(c.getColumnIndex(DETAILS)));
                events[arrIndex++] = temp;
                c.move(1);
             }
        }
        sqLiteDatabase.close();
        return events;
    }

    //retrieve all events
    public Event[] allEvents(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_EVENTS +";";


        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        Event[] events = new Event[cursor.getCount()];
        cursor.moveToFirst();
        int arrIndex = 0;


        for(int i=0; i<cursor.getCount();i++) {
            if (cursor.getString(cursor.getColumnIndex(TITLE)) != null) {
                Event temp = new Event();
                temp.setId(cursor.getString(cursor.getColumnIndex("_id")));
                temp.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                temp.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                temp.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                temp.setDetails(cursor.getString(cursor.getColumnIndex(DETAILS)));
                events[arrIndex++] = temp;
                cursor.move(1);
            }
        }

        sqLiteDatabase.close();
        return events;
    }

    //get appointment by ID
    public Event getEventByID(String id){
        Event event = new Event();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = ("SELECT * FROM " + TABLE_EVENTS + " WHERE " + ID + " LIKE '%" + id + "%'");

        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        cursor.moveToFirst();
        for(int i=0; i<cursor.getCount();i++) {
            if (cursor.getString(cursor.getColumnIndex(TITLE)) != null) {
                Event temp = new Event();
                temp.setId(cursor.getString(cursor.getColumnIndex("_id")));
                temp.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                temp.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                temp.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                temp.setDetails(cursor.getString(cursor.getColumnIndex(DETAILS)));
                event = temp;
                cursor.move(1);
            }
        }
        sqLiteDatabase.close();
        return event;
    }







}
