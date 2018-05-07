/*
 * Crime Class UML
 * *************Properties************
 * -latitude: String
 * -longitude: String
 * -date: String
 * -offense: String
 * -description: String
 * -street: String
 * -initial: String
 * -offenseCode: int
 * -color: static String
 **************Constructor************
 * Crime(input: ArrayList<String>)
 ******************Methods************
 * Note: Setters and Getters are excluded
 * +getMarker(index: int): String
 * +toString():String
 */
package bostoncrimemap;
import java.util.ArrayList;
public class Crime {

private String 
            latitude,
            longitude,
            date,
            offense,
            description,
            street,
            initial;  
private int offenseCode;
private static String color = "color:red";      // Sets color for pin

Crime(ArrayList<String> input){
    offenseCode = Integer.parseInt(input.get(1));
    offense = input.get(2);
    description = input.get(3);
    date = input.get(7);
    street = input.get(13);
    latitude = input.get(14);
    longitude = input.get(15);  
    
}

// Setters and getters 

public String getLatitude(){
return this.latitude;
}
public void setLatitude(String latitude){
this.latitude = latitude;
}
public String getLongitude(){
return this.longitude;
}
public void setLongitude(String longitude){
this.longitude = longitude;
}
public String getDate(){
return this.date;
}
public void setDate(String date){
this.date = date;
}
public String getOffense(){
return this.offense;
}
public void setOffense(String offense){
this.offense = offense;
}
public String getDescription(){
return this.description;
}
public void setDescription(String description){
this.description = description;
}
public String getStreet(){
return this.street;
}
public void setStreet(String street){
this.street = street;
}
public String getInitial(){
return this.initial;
}
public void setInitial(String initial){
this.initial = initial;
}
public int getOffenseCode(){
return this.offenseCode;
}
public void setOffenseCode(int offenseCode){
this.offenseCode = offenseCode;

}
public String getColor(){
return Crime.color;
}


public String getMarker(int i){     // returns the URL segment for the given Crime for Google Static Maps API
    Character initialChar = (char) ('A' + i);   // Set first pin to A then add one to get next letters
 
    this.setInitial(initialChar.toString());    // Pin initial must be converted to string type
    
    // URL encoding. %7c is the encoding for the '|' character and seperates all parameters
    return "&markers=" + color + "%7C" + "label:" + initial + "%7C" + latitude + "," + longitude;   
}
@Override
public String toString(){
return "Offense: " + offense + ", description: " + description.toLowerCase() + "\nDate: " + date + 
        "\nStreet: " + street.toLowerCase() + ", Coordinates: " + latitude + "," + longitude;
} 
}
