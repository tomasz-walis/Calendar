package org.com.calendar;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;


public class DeleteEvent extends Activity {

    private String date;
    private SqlBdHelper dbEvent;
    private Event[] events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        Intent curIntent = getIntent();
        date = curIntent.getStringExtra("date");
        TextView deleteDate = (TextView) findViewById(R.id.deleteDate);
        deleteDate.setText("Date: "+ date);
        dbEvent = new SqlBdHelper(this,null,null,1);
        eventList();
    }

    //list of events when user selects event to delete
    public void eventList(){

        events = dbEvent.selectEventOnDate(date);

        ListView list = (ListView) findViewById(R.id.deleteList);
        ListAdapter adapter = new CustomAdapter(this, events);
        list.setAdapter(adapter);

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Event clickedItem = (Event) adapterView.getItemAtPosition(i);
                        final String eventId = clickedItem.getId();
                        String event = clickedItem.getTitle();
                        new AlertDialog.Builder(DeleteEvent.this)
                                .setTitle("Confirm Delete")
                                .setMessage("Do you want to delete selected event ?: \""+ event +"\" ?")
                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbEvent.deleteSelectedEventOnDate(eventId);
                                        eventList();
                                    }
                                })

                                .show();
                    }
                }

        );

    }

}
