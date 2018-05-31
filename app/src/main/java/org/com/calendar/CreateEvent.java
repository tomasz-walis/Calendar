package org.com.calendar;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class CreateEvent extends Activity {

    private TextView txtDate, txtTime;
    private String time, date;
    private int hours, minutes;
    private SqlBdHelper dbEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        txtDate = (TextView) findViewById(R.id.choosenDate);
        txtTime = (TextView) findViewById(R.id.time);
        txtDate.setText("Date: "+date);
        dbEvent = new SqlBdHelper(this,null,null,1);
    }

    //show time picker when button pressed
    public void showTimePicker(View v){
        showDialog(0);
    }
    //create dialog from time picker
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == 0)return new TimePickerDialog(CreateEvent.this, timePickerListner,hours, minutes,true);
        return null;
    }

    //select time on time picker
    protected TimePickerDialog.OnTimeSetListener timePickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            hours = hour;
            minutes = minute;
            time = hour+":"+minute;
            txtTime.setText("Time: "+hour+":"+minute);
        }
    };

    //add events when save clicked
    public void save(View v){
        EditText txtTitle = (EditText) findViewById(R.id.txtDetails);
        EditText txtDetails = (EditText) findViewById(R.id.details);

        Event event = new Event(txtTitle.getText().toString(),date,time,txtDetails.getText().toString());
        boolean titleExist = dbEvent.checkIfEventExist(txtTitle.getText().toString(),date);
        if (titleExist==true){
            Toast.makeText(CreateEvent.this, "Title Already Exists Please Choose Different Title", Toast.LENGTH_SHORT).show();
        }else {
            dbEvent.addEvent(event);
            Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    //retrieve and show synonyms in the text edit
    static String synonyms = "";
    public void thesaurus(View v){
        EditText txtThesaurusText = (EditText) findViewById(R.id.thesaurusText);
        final String word = txtThesaurusText.getText().toString();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                ArrayList<String> synonyms = new Thesaurus().SendRequest(word);
                    CreateEvent.synonyms = "";
                for(String s:synonyms){
                    CreateEvent.synonyms += s+"\n";
                }
            }
        });
        thread.start();

        try {
            thread.join();
            update(synonyms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(String s){
        final EditText txtSynonyms = (EditText) findViewById(R.id.synonyms);
        txtSynonyms.setText(s);

    }

    //replaceWord word when it is highlited and button clicked
    public void highlightedWord(View v){

        EditText txtDetails=(EditText)findViewById(R.id.details);
        int startSelection=txtDetails.getSelectionStart();
        int endSelection=txtDetails.getSelectionEnd();
        String selectedText = txtDetails.getText().toString().substring(startSelection, endSelection);

        if(selectedText.matches("[a-zA-Z]+$")){

            replaceWord(selectedText);

        }else{
            Toast.makeText(getApplicationContext(), "Select word.\n  Text only!", Toast.LENGTH_LONG).show();
        }


    }

    //replaceWord word from thesaurus
    public void replaceWord(final String word){


        final ArrayList<String> synonymList = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                ArrayList<String> synonyms = new Thesaurus().SendRequest(word);
                for(String s:synonyms){
                    synonymList.add(s);
                }
            }
        });
        thread.start();

        try {
            thread.join();
            final CharSequence[] list = new CharSequence[synonymList.size()];
            int i =0;
            for(String s:synonymList){
                list[i++] = s;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Select");
            builder.setItems(list, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    EditText txtDetails=(EditText)findViewById(R.id.details);
                     /*returns the greater of two int values. That is, the result is the argument closer to positive infinity.
                     If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN.
                     Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero.
                     If one argument is positive zero and the other negative zero, the result is positive */
                    int start = Math.max(txtDetails.getSelectionStart(), 0);
                    int end = Math.max(txtDetails.getSelectionEnd(), 0);
                    txtDetails.getText().replace(Math.min(start, end), Math.max(start, end),list[item].toString(), 0, list[item].toString().length());



                }
            });
            AlertDialog alert = builder.create();
            alert.show();



        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }





}


