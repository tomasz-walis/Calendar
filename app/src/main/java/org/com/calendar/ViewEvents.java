package org.com.calendar;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;


public class ViewEvents extends Activity {
    private String date;
    private SqlBdHelper dbEvent;
    private Event[] events;
    private Event clickedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setTitle("View Event");
        Intent curIntent = getIntent();
        date = curIntent.getStringExtra("date");
        TextView moveDate = (TextView) findViewById(R.id.ViewDate);
        moveDate.setText("Date: "+ date);
        dbEvent = new SqlBdHelper(this,null,null,1);
        createList();
    }

    public void createList(){
        events = dbEvent.selectEventOnDate(date);

        ListView list = (ListView) findViewById(R.id.ViewList);

        ListAdapter adapter = new CustomAdapter(this, events);
        if (adapter.isEmpty()){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Event");
            alertDialog.setMessage("No events available to view on date : " + date);
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
                        final String Item_id = clickedItem.getId();
                        itemClicked(Item_id);
                    }
                }

        );

    }

    public void itemClicked(String id){

        Intent intent = new Intent(this, EditEvent.class);
        intent.putExtra("date",date);
        intent.putExtra("id",id);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

       finish();
    }
}
