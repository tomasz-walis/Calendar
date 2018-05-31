package org.com.calendar;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class Thesaurus  {

    //main link to the webservice
    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1";


    //send connection request to the webservice
    public ArrayList<String> SendRequest(String word) {
        ArrayList<String> synonyms = new ArrayList() ;
        try {
            URL serverAddress = new URL(endpoint + "?word="+word+"&language=en_US&key=6ZJNHXJ2aOdZ6rFYIE90&output=xml");
            HttpURLConnection connection = (HttpURLConnection)serverAddress.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                try{
                    //add synonym list to the array
                    String result = line.substring(line.indexOf("<synonyms>") +10, line.indexOf("</synonyms>"));
                    String[] temp = result.split(Pattern.quote("|"));
                    for(int i =0; i<temp.length;i++){
                        synonyms.add(temp[i]);
                    }


                }catch(Exception e){}

            }

            //disconnect the webservice connection
            connection.disconnect();
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        } catch (java.net.ProtocolException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


        return synonyms;

    }


}
