package org.com.calendar;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;


public class EditEvent extends Activity {
    private TextView txtDate, txtTime;
    private String time, date, id ;
    private int hours, minute;
    private SqlBdHelper dbEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dbEvent = new SqlBdHelper(this,null,null,1);

        Intent curIntent = getIntent();
        date = curIntent.getStringExtra("date");
        id = curIntent.getStringExtra("id");
        updateView();


        txtDate = (TextView) findViewById(R.id.editViewOnDate);
        txtDate.setText("Date: "+date);
    }

    //show event in to the text fields
    public void updateView(){
        txtTime = (TextView) findViewById(R.id.editTime);
        EditText txtTitleEdit = (EditText) findViewById(R.id.editTitleDetails);
        EditText txtDetailsEdit = (EditText) findViewById(R.id.editDetails);
        Event event = dbEvent.getEventByID(id) ;
        txtTime.setText(event.getTime());
        txtTitleEdit.setText(event.getTitle());
        txtDetailsEdit.setText(event.getDetails());
    }

    //show the time picker on the edit event activity
    public void editTimeButton(View v){
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == 0)return new TimePickerDialog(EditEvent.this, timePickerListner,hours, minute,true);
        return null;
    }


    protected TimePickerDialog.OnTimeSetListener timePickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            hours = hour;
            EditEvent.this.minute = minute;
            time = hour+":"+minute;
            txtTime.setText("Time: "+hour+":"+minute);
        }
    };

    //method for edit events when the edit event button is clicked
    public void editEvent(View v){
        dbEvent.deleteSelectedEventOnDate(id);

        EditText scrTitle = (EditText) findViewById(R.id.editTitleDetails);
        EditText scrDetails = (EditText) findViewById(R.id.editDetails);

        Event event = new Event(scrTitle.getText().toString(),date,time,scrDetails.getText().toString());
        dbEvent.addEvent(event);

        Toast.makeText(getApplicationContext(), "Event Edited", Toast.LENGTH_LONG).show();

        finish();


    }



}
