package org.com.calendar;

import android.content.*;
import android.view.*;
import android.widget.*;


class CustomAdapter extends ArrayAdapter<Event> {
    private int itemNumber = 1;
    private Event[] createdEvent;

    //Custom list view adapter
    public CustomAdapter(Context context, Event[] events) {
        super(context,R.layout.custom_row, events);
        createdEvent = events;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row,parent,false);

        Event event = getItem(position);
        TextView txtItemNumber = (TextView) customView.findViewById(R.id.listItemNo);
        TextView txtItemName = (TextView) customView.findViewById(R.id.listItemName);
        TextView txtItemDetails = (TextView) customView.findViewById(R.id.listItemDetails);
        TextView txtItemDetails2 = (TextView) customView.findViewById(R.id.listItemDetails2);


        txtItemNumber.setText(Integer.toString(itemNumber)+". ");
        if(itemNumber < createdEvent.length){
            itemNumber++;}else {
            itemNumber = 1 ;}
        txtItemName.setText(event.getTitle());
        txtItemDetails.setText("Time: "+ event.getTime());
        txtItemDetails2.setText("Date: "+ event.getDate());
        return customView;

    }
}
