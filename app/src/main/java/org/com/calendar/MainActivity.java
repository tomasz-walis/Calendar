package org.com.calendar;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.text.*;
import java.util.*;

public class MainActivity extends Activity {

    private String currentDate;
    private SqlBdHelper dbEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(new Date(calendar.getDate()));

        //main calendar when application starts
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int day) {
                month++;
                String stringMonth;
                String stringDay;
                if(month<10){stringMonth ="0"+ String.valueOf(month);} else{stringMonth = String.valueOf(month);}
                if(day<10){stringDay ="0"+ String.valueOf(day);} else{stringDay = String.valueOf(day);}

                currentDate =stringDay+"/"+stringMonth+"/"+ String.valueOf(year);
                Toast.makeText(getApplicationContext(), "Selected Date:\n" + "Day = " + day + "\n" + "Month = " + month + "\n" + "Year = " + year, Toast.LENGTH_LONG).show();
                }
        });
        if(currentDate ==null){
            currentDate =selectedDate;}
        dbEvent = new SqlBdHelper(this,null,null,1);

    }

    //open view events activity when view/edi button clicked
    public void viewEdit(View v){
        Intent intent = new Intent(this, ViewEvents.class);
        intent.putExtra("date", currentDate);
        startActivity(intent);
    }

    //open create even activity
    public void create(View v){
        Intent intent = new Intent(this, CreateEvent.class);
        intent.putExtra("date", currentDate);
        startActivity(intent);
    }


    //show the delete dialog to the user when delete event button pressed
    public void delete(View v){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Options")
                .setPositiveButton("Delete all events on date", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Confirm Delete")
                                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbEvent.deleteAllEvents(currentDate);
                                    }
                                })

                                .show();

                    }
                })
                .setNegativeButton("Select event to delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectDelete();
                    }
                })

                .show();
    }

    //open delete activity when user presses select event to delete
    public void selectDelete(){
        Intent intent = new Intent(this, DeleteEvent.class);
        intent.putExtra("date", currentDate);
        startActivity(intent);
    }

    //open search activity
    public void search(View v){
        Intent intent = new Intent(this, SearchEvent.class);
        startActivity(intent);
    }

    //open move event activity
    public void move(View v){
        Intent intent = new Intent(this, MoveEvent.class);
        intent.putExtra("date", currentDate);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
