package org.com.calendar;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class SearchEvent extends Activity {

    private Event[] events;
    private SqlBdHelper dbEvent;
    private EditText search;
    private Event eventClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbEvent = new SqlBdHelper(this,null,null,1);
        search = (EditText) findViewById(R.id.searchText);
        //retrieve all events from data base
        events = dbEvent.allEvents();
        createEventsList(events);

            //observe the text field for any text changes
            search.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s){}

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    createEventsList(events);
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    search(s);
                }
            });
    }

    //search function when user types in to the search txt
    public void search(CharSequence find){
        ArrayList<Event> eventFind = new ArrayList<Event>();
        for(int i = 0; i< events.length; i++){
            if(events[i].getTitle().contains(find)){
                eventFind.add(events[i]);
            }else{
                if(events[i].getDetails().contains(find)){
                    eventFind.add(events[i]);
                }
            }
        }

        if(!eventFind.isEmpty() ){
           Event[] toUpdate = new Event[eventFind.size()];
            createEventsList(eventFind.toArray(toUpdate));

        }

    }

    //show the list of events to the user
    public void createEventsList(Event[] input){

        ListView list = (ListView) findViewById(R.id.searchList);
        ListAdapter adapter = new CustomAdapter(this,input);
        if (adapter.isEmpty()){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Event");
            alertDialog.setMessage("No events available: ");
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
                        eventClicked = (Event) adapterView.getItemAtPosition(i);
                        final String eventId = eventClicked.getId();
                        String date = eventClicked.getDate();
                        eventClicked(eventId,date);
                    }
                }

        );

    }
    //show edit activity when user clicks on the event
    public void eventClicked(String id, String date){

        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra("date",date);
        intent.putExtra("id",id);
        startActivity(intent);

    }


    }



