package org.com.calendar;


import android.provider.BaseColumns;



public interface DBConstants extends BaseColumns {

    //data base constants
    String DATABASE_NAME = "events.db";
    String TABLE_EVENTS = "events";
    String ID = "_id";
    String TITLE = "title";
    String DATE = "date";
    String TIME = "time";
    String DETAILS = "details";
}