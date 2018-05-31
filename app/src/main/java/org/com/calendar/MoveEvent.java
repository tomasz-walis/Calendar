package org.com.calendar;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MoveEvent extends Activity {

    private String date, movedDate;
    private SqlBdHelper dbEvent;
    private Event[] events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        Intent curIntent = getIntent();
        date = curIntent.getStringExtra("date");

        TextView moveDate = (TextView) findViewById(R.id.moveDate);
        moveDate.setText("Date: "+ date);


        dbEvent = new SqlBdHelper(this,null,null,1);
        createList();
    }
    Event clickedItem;

    //populate list view on move activity
    public void createList(){
        events = dbEvent.selectEventOnDate(date);

        ListView list = (ListView) findViewById(R.id.moveList);
        ListAdapter adapter = new CustomAdapter(this, events);
        if (adapter.isEmpty()){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Event");
            alertDialog.setMessage("No events available to move on date : " + date);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.show();
        }
        list.setAdapter(adapter);

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        clickedItem = (Event) adapterView.getItemAtPosition(i);

                        String Item = clickedItem.getTitle();
                        new AlertDialog.Builder(MoveEvent.this)
                                .setTitle("Move Event?")
                                .setMessage("Do you want to Move: \""+ Item +"\" ?")
                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        showDialog(0);
                                    }
                                })

                                .show();
                    }
                }

        );

    }
    Calendar calendar = Calendar.getInstance();
    int currentYear = calendar.get(Calendar.YEAR);
    int currentMonth = calendar.get(Calendar.MONTH);
    int currentDate = calendar.get(Calendar.DAY_OF_MONTH);


    //show date picker on the move activity
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == 0)return new DatePickerDialog(MoveEvent.this, datePickerListner, currentYear, currentMonth, currentDate);
        return null;
    }


    protected DatePickerDialog.OnDateSetListener datePickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker timePicker, int year, int month, int day) {
            month++;
            String stringMonth;
            String stringDay;
            if(month<10){stringMonth ="0"+ String.valueOf(month);} else{stringMonth = String.valueOf(month);}
            if(day<10){stringDay ="0"+ String.valueOf(day);} else{stringDay = String.valueOf(day);}

            movedDate =stringDay+"/"+stringMonth+"/"+ String.valueOf(year);

            Toast.makeText(getApplicationContext(), movedDate, Toast.LENGTH_LONG).show();
            dbEvent.moveDate(clickedItem.getId(), movedDate);
            createList();

        }
    };
}
